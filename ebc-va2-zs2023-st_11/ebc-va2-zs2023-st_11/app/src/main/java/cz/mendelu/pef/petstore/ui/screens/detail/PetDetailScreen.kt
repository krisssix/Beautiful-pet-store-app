package cz.mendelu.pef.petstore.ui.screens.detail

import android.webkit.URLUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.compose.todo.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.constants.PlaceholderConstants
import cz.mendelu.pef.petstore.destinations.EditPetScreenDestination
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.ShadowBox
import cz.mendelu.pef.petstore.ui.screens.add.EditPetViewModel
import cz.mendelu.pef.petstore.ui.theme.Purple20
import cz.mendelu.pef.petstore.ui.theme.Purple40
import cz.mendelu.pef.petstore.ui.theme.Purple60


@Composable
@Destination
fun PetDetailScreen(
    navigator: DestinationsNavigator,
    id: Long,
){
    val viewModel = hiltViewModel<PetDetailViewModel>()
    val viewModelSec = hiltViewModel<EditPetViewModel>()

    val uiState: MutableState<UiState<Pet, DetailErrors>> = rememberSaveable {
        mutableStateOf(UiState())
    }
    viewModel.let{
        LaunchedEffect(it){
            it.loadPet(id)
        }
    }
    viewModel.petDetailUIState.value.let {
        uiState.value = it
    }

    BaseScreen(
        topBarText = "${uiState.value.data?.name ?: "Pet"}'s details",
        drawFullScreenContent = false,
        showLoading = uiState.value.loading,
        actions = {
            IconButton(
                onClick = { viewModel.deletePet(id); navigator.popBackStack() },
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
            IconButton(
                onClick = {
                    navigator.navigate(EditPetScreenDestination(id = id))
                },
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        },
        placeholderScreenContent =
        if (uiState.value.errors != null)
            PlaceholderScreenContent(
                image = null,
                text = stringResource(id = uiState.value.errors!!.communicationError)
            )
        else null,
        onBackClick = { navigator.popBackStack() }) {

        viewModel.petDetailUIState.value.data?.let { pet ->
            PetDetailScreenContent(
                paddingValues = it,
                uiState = uiState,
                viewModel = viewModel,
                navigator = navigator,
                pet = pet
            )
        }
    }
}


@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    uiState: MutableState<UiState<Pet, DetailErrors>>,
    viewModel: PetDetailViewModel,
    navigator: DestinationsNavigator,
    pet: Pet
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(color = Purple60, shape = RoundedCornerShape(15.dp))
           // .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Purple40)
            .clip(RoundedCornerShape(28.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PetImage(url = pet.photoUrls?.firstOrNull())
            PetName(name = pet.name ?: "Unknown pet")
            ShadowBox()
            PetInfo(label = "Status", value = pet.status ?: "Unknown Status")
            ShadowBox()
            PetInfo(label = "Category", value = pet.category?.name ?: "No Category")
            ShadowBox()
            PetInfo(label = "Tags", value = pet.tags?.joinToString(", ") { it.name ?: "" } ?: "No Tags")

        }

    }
}

@Composable
fun PetName(name: String?) {
    Text(
        text = name?.takeIf { it.isNotEmpty() } ?: "No name",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        maxLines = 2,
        color = Color.Black,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun PetImage(url: String?)
             //imagePlaceholder: Any
{
    val myPlaceholders = PlaceholderConstants.PLACEHOLDERS
    //val randomPlaceholder = myPlaceholders.random()
    val imagePlaceholder = "https://cdn.discordapp.com/attachments/1087764824630509709/1166887636162592808/OIG_16.png?ex=654c1fcc&is=6539aacc&hm=ccccef9457522857a26b698003ce9ceaf88693cf21f01b8b95ec1065d78c2a20&"
    val imageUrl = if (URLUtil.isValidUrl(url)) {
        url
    } else {
        imagePlaceholder
        //randomPlaceholder
    }
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(28.dp))
           // .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Purple40)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PetInfo(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Purple20, fontWeight = FontWeight.Medium,maxLines = 2,)
        Text(text = value, color = Purple40, fontWeight = FontWeight.Normal,maxLines = 2,)
    }
}
