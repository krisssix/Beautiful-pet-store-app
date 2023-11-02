package cz.mendelu.pef.petstore.ui.screens.listofpets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.compose.todo.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.theme.Purple40
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import coil.compose.rememberImagePainter
import cz.mendelu.pef.petstore.constants.PlaceholderConstants
import cz.mendelu.pef.petstore.destinations.AddPetScreenDestination
import cz.mendelu.pef.petstore.destinations.PetDetailScreenDestination
import cz.mendelu.pef.petstore.ui.theme.Pink40
import cz.mendelu.pef.petstore.ui.theme.Purple20
import cz.mendelu.pef.petstore.ui.theme.Purple80
import cz.mendelu.pef.petstore.utils.rememberLifecycleEvent


@Destination("ListOfPets")
@Composable
fun ListOfPetsScreen(
    navigator: DestinationsNavigator
) {

    val viewModel = hiltViewModel<ListOfPetsViewModel>()
    val uiState: MutableState<UiState<List<Pet>, ListOfPetsErrors>> =
        rememberSaveable {
            mutableStateOf(UiState())
        }
    viewModel.petsUIState.value.let {
        uiState.value = it
    }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            viewModel.loadPets()
        }
    }

    BaseScreen(
        topBarText = "List of Pets",
        drawFullScreenContent = true,
        showLoading = uiState.value.loading,
        placeholderScreenContent = if (uiState.value.errors != null) {
            PlaceholderScreenContent(null,
                stringResource(id = uiState.value.errors!!.communicationError))
        } else
            null,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                     navigator.navigate(AddPetScreenDestination)
                },
                containerColor = Purple40
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {
        ListOfPetsScreenContent(
            paddingValues = it,
            uiState = uiState.value,
            pets = viewModel.pets.value,
            navigator = navigator,
            viewModel = viewModel)
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<List<Pet>, ListOfPetsErrors>,
    navigator: DestinationsNavigator,
    pets: List<Pet>,
    viewModel: ListOfPetsViewModel
) {

    val isRefreshed = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { viewModel.loadPets() },
        refreshThreshold = 8.dp
    )
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
    ) {
        if (isRefreshed.progress > 1) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .wrapContentSize(Alignment.Center),
                color = Pink40
            )
        }
    LazyColumn(
        modifier = Modifier.pullRefresh(state = isRefreshed))
         {
        if(uiState.data != null){
            uiState.data!!.reversed().forEach{
                item {
                    PetRowBackground {
                        PetRow(
                            pet = it,
                            onClick = {
                                navigator.navigate(
                                    PetDetailScreenDestination(id = it.id!!)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PetImageIcon(pet: Pet) {
    val firstPhotoUrl = pet.photoUrls?.firstOrNull()
    val myPlaceholders = PlaceholderConstants.PLACEHOLDERS

    val randomPlaceholder = myPlaceholders.random()

    if ((firstPhotoUrl == null) || !firstPhotoUrl.contains("http")) {
        Image(
            painter = rememberAsyncImagePainter(model = randomPlaceholder),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp, 80.dp)
                .clip(RoundedCornerShape(16.dp))

        )
    } else {
        Image(
            painter = rememberAsyncImagePainter(
                pet.photoUrls!![0]
            ), contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp, 80.dp)
                .clip(RoundedCornerShape(16.dp))
        )


    }
}

@Composable
fun FavouriteTag(pet: Pet) {
    ChipView(isFavourite = "available", colorResource = Purple20, pet = pet)
}

@Composable
fun ChipView(
    isFavourite: String,
    colorResource: Color,
    pet: Pet) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource.copy(.08f))

    ) {
        Text(
            text = pet.status!!,
            modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                letterSpacing = 0.5.sp, // Slight letter spacing for visual enhancement
                fontWeight = FontWeight.Bold
            ),
            color = colorResource
        )
    }
}

@Composable
fun PetRowBackground(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Purple40),
        shape = RoundedCornerShape(12.dp)
    ) {
        content()
    }
}

@Composable
fun PetRow(
    pet: Pet,
    onClick: (() -> Unit),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PetImageIcon(pet)

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = pet.name ?: "Unknown",
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.material.MaterialTheme.typography.h6
            )

            Text(
                text = stringResource(R.string.view_detail),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.material.MaterialTheme.typography.caption
            )

            if (pet.status!!.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FavouriteTag(pet = pet)
                }
            }

        }
    }
}









