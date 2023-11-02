package cz.mendelu.pef.petstore.ui.screens.add

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.screens.detail.DetailErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditPetViewModel @Inject constructor(
    private val petsRemoteRepositoryImpl: PetsRemoteRepositoryImpl
) : BaseViewModel() {

    val editPetUIState: MutableState<UiState<Pet, EditPetErrors>> = mutableStateOf(UiState())


    fun editPet(pet: Pet){
        launch{
            val result = withContext(Dispatchers.IO){
                petsRemoteRepositoryImpl.addPet(pet)
            }
            when(result){
                is CommunicationResult.CommunicationError -> editPetUIState.value = UiState(loading = false, data = null, errors = EditPetErrors(
                    R.string.no_internet_connection)
                )
                is CommunicationResult.Error -> editPetUIState.value = UiState(loading = false, data = null, errors = EditPetErrors(
                    R.string.failed_to_load_the_list)
                )
                is CommunicationResult.Exception -> editPetUIState.value = UiState(loading = false, data = null, errors = EditPetErrors(
                    R.string.unknown_error)
                )
                is CommunicationResult.Success -> editPetUIState.value = UiState(loading = false, data = result.data)
            }
        }
    }


}