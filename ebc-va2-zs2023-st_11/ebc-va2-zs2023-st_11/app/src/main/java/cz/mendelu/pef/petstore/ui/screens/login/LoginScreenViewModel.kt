package cz.mendelu.pef.petstore.ui.screens.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.users.UsersRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val usersRemoteRepositoryImpl: UsersRemoteRepositoryImpl
) : BaseViewModel(), LoginScreenActions {


    val isSignInButtonEnabled: MutableState<Boolean> = mutableStateOf(true)
    val usersUIState: MutableState<UiState<User, LoginErrors>> = mutableStateOf(UiState(loading = false))


   override fun login(username: String, password: String, onSuccess: () -> Unit) {
       if (!isUserNameValid(username) || !isPasswordValid(password)) {
           usersUIState.value = UiState(
               loading = false,
               data = null,
               errors = LoginErrors(
                   usernameError = "Invalid e-mail format",
                    passwordError = "Password does not have more than 7 signs"
                   )
           )
       } else {
            usersUIState.value = UiState(loading = true, data = null,errors = null)
            launch {
                val result = withContext(Dispatchers.IO) {
                    usersRemoteRepositoryImpl.login(username = username, password = password)
                }
                when (result) {
                    is CommunicationResult.CommunicationError -> {
                        usersUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = LoginErrors(R.string.no_internet_connection)
                        )
                    }
                    is CommunicationResult.Error -> {
                        usersUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = LoginErrors(R.string.failed_to_load_the_list)
                        )
                    }
                    is CommunicationResult.Exception -> {
                        usersUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = LoginErrors(R.string.unknown_error)
                        )
                    }
                    is CommunicationResult.Success -> {
                        usersUIState.value = UiState(
                            loading = false,
                            data = result.data,
                            errors = null
                        )
                        onSuccess()
                    }
                }
            }
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }

    fun areInputsValid(username: String, password: String): Boolean {
        val isValid = isUserNameValid(username) && isPasswordValid(password)
        isSignInButtonEnabled.value = isValid
        return isValid
    }

}
