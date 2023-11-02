package cz.mendelu.pef.petstore.ui.screens.edit


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavArgs
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.compose.todo.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.model.Category
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.Tag
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.RoundButton
import cz.mendelu.pef.petstore.ui.elements.TextInputField
import cz.mendelu.pef.petstore.ui.screens.add.EditPetErrors
import cz.mendelu.pef.petstore.ui.screens.add.EditPetViewModel
import cz.mendelu.pef.petstore.ui.screens.detail.DetailErrors
import cz.mendelu.pef.petstore.ui.screens.detail.PetDetailViewModel
import cz.mendelu.pef.petstore.ui.theme.Purple40
import cz.mendelu.pef.petstore.ui.theme.basicMargin


@Destination
@Composable
fun EditPetScreen(
    navigator: DestinationsNavigator,
    id: Long,
){
    val viewModelSec = hiltViewModel<EditPetViewModel>()
    val viewModel = hiltViewModel<PetDetailViewModel>()


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
        topBarText = "Edit ${uiState.value.data?.name ?: "Pet"}",
        drawFullScreenContent = false,
        showLoading = false,
        placeholderScreenContent =
        if (uiState.value.errors != null)
            PlaceholderScreenContent(
                image = null,
                text = stringResource(id = uiState.value.errors!!.communicationError)
            )
        else null,
        onBackClick = { navigator.popBackStack() }) {
        EditScreenContent(
            viewModel = viewModel,
            viewModelSec = viewModelSec,
            navigator = navigator,
            id = id
        )
    }

}


@Composable
fun EditScreenContent(
    viewModelSec: EditPetViewModel,
    viewModel: PetDetailViewModel,
    navigator: DestinationsNavigator,
    id: Long,
){
    var petName by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var tag by rememberSaveable { mutableStateOf("") }

    MainViews()

    Column(
        modifier = Modifier.padding(all = basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputField(
            value = petName,
            hint = "Pet Name",
            onValueChange = { petName = it },
            errorMessage = if (petName.isNotEmpty()) null else "Name of pet"
        )

        TextInputField(
            value = category,
            hint = "Category",
            onValueChange = { category = it },
            errorMessage = if (category.isNotEmpty()) null else "Pet Category"
        )

        TextInputField(
            value = tag,
            hint = "Tag",
            onValueChange = { tag = it }
        )
        RoundButton(
            onClick = {
                if(petName.isNotEmpty() && category.isNotEmpty()) {
                    val updatedPet = Pet(
                        id = id,
                        category = Category(0, category),
                        name = petName,
                        photoUrls = listOf("string"),
                        tags = listOf(Tag(0, tag)),
                        status = "available"
                    )
                    viewModelSec.editPet(updatedPet)
                    navigator.popBackStack()
                }
                else{
                    "This pet cannot be saved"
                }
            },
            text = "Edit Pet",
            modifier = Modifier.padding(8.dp)
        )
    }
}


@Composable
fun MainViews(){
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(28.dp))
            .shadow(2.dp, RoundedCornerShape(5.dp), spotColor = Purple40, )
    ) {
        Image(
            painter = rememberImagePainter(data = "https://cdn.discordapp.com/attachments/1087764824630509709/1166859256763531326/OIG_14.png?ex=654c055e&is=6539905e&hm=285715d1f2567eab9cd62d2e94c24dd949e0989b173fa5d673e10ff4155ac782&"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
