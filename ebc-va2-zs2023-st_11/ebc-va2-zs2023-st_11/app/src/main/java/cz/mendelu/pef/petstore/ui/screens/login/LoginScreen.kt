package cz.mendelu.pef.petstore.ui.screens.login

import android.util.Log
import android.webkit.URLUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.compose.todo.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.model.User
import cz.mendelu.pef.petstore.ui.elements.BaseScreen
import cz.mendelu.pef.petstore.ui.elements.RoundButton
import cz.mendelu.pef.petstore.ui.elements.ShadowBox
import cz.mendelu.pef.petstore.ui.elements.TextInputField
import cz.mendelu.pef.petstore.ui.theme.Purple40
import cz.mendelu.pef.petstore.ui.theme.basicMargin



@Destination(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
) {


    val viewModel = hiltViewModel<LoginScreenViewModel>()

    val uiState: MutableState<UiState<User, LoginErrors>> = rememberSaveable {
        mutableStateOf(UiState())
    }

    viewModel.usersUIState.value.let {
        uiState.value = it
    }


    BaseScreen(
        topBarText = "Login",
        showLoading = uiState.value.loading,
        placeholderScreenContent = if (uiState.value.errors != null) {
            PlaceholderScreenContent(null, uiState.value.errors!!.communicationError?.let { stringResource(id = it) })
        } else null
    ) {
        LoginScreenContent(paddingValues = it,navigator=navigator, uiState = uiState.value, viewModel = viewModel)
    }

}


@Composable
fun LoginScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<User, LoginErrors>,
    navigator: DestinationsNavigator,
    viewModel: LoginScreenViewModel
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

   MainViews()
    Column(
        modifier = Modifier.padding(all = basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        TextInputField(
            value = username,
            hint = stringResource(R.string.email),
            onValueChange = { newValue ->
                username = newValue
                viewModel.areInputsValid(username, password)
            },
            errorMessage = uiState.errors?.usernameError)

            TextInputField(
                value = password,
                hint = stringResource(R.string.password),
                onValueChange = { newValue ->
                    password = newValue
                    viewModel.areInputsValid(username, password)
                },
                keyboardType = KeyboardType.Password,
                errorMessage = uiState.errors?.passwordError)

            Spacer(modifier = Modifier.width(16.dp))

            RoundButton(
                text = stringResource(R.string.sign_in),
                enabled = viewModel.isSignInButtonEnabled.value,
                onClick = {
                    if (viewModel.areInputsValid(username, password)) {
                        viewModel.login(
                            username = username,
                            password = password,
                            onSuccess = { navigator.navigate(route = "ListOfPets") }
                        )
                    }
                }
            )


        // response vždy 200, i když udělám usernameError/passwordError -> vždy success, chyba se neukáže
            if (uiState.errors != null) {
                val errorMsg = when {
                    uiState.errors!!.usernameError != null -> uiState.errors!!.usernameError
                    uiState.errors!!.passwordError != null -> uiState.errors!!.passwordError
                    else -> null
                }
                if (errorMsg != null) {
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        action = {},
                        content = { Text(text = errorMsg) }
                    )
                }
            }

        }


}


@Composable
fun MainViews(){
    ShadowBox()
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(28.dp))
            //.shadow(2.dp, RoundedCornerShape(5.dp), spotColor = Purple40, )
    ) {
        Image(
            painter = rememberImagePainter(data = "https://cdn.discordapp.com/attachments/1087764824630509709/1166835934063497317/OIG_12.png?ex=654befa5&is=65397aa5&hm=0bfa04f49d5e6394070190e4ed208e152944a86ba5894dc91f8d4a14ce0a1d2a&"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    ShadowBox()
}

