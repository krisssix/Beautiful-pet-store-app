package cz.mendelu.pef.petstore.ui.screens.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import cz.mendelu.pef.petstore.ui.screens.add.EditPetErrors
import cz.mendelu.pef.petstore.ui.screens.listofpets.ListOfPetsErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val petsRemoteRepositoryImpl: PetsRemoteRepositoryImpl
) : BaseViewModel(){
    val petDetailUIState: MutableState<UiState<Pet, DetailErrors>> = mutableStateOf(UiState())

    fun loadPet(id: Long){
        launch{
            val result = withContext(Dispatchers.IO){
                petsRemoteRepositoryImpl.findById(id)
            }
            when(result){
                is CommunicationResult.CommunicationError -> petDetailUIState.value = UiState(loading = false, data = null, errors = DetailErrors(
                    R.string.no_internet_connection)
                )
                is CommunicationResult.Error -> petDetailUIState.value = UiState(loading = false, data = null, errors = DetailErrors(
                    R.string.failed_to_load_the_list))
                is CommunicationResult.Exception -> petDetailUIState.value = UiState(loading = false, data = null, errors = DetailErrors(
                    R.string.unknown_error))
                is CommunicationResult.Success -> petDetailUIState.value = UiState(loading = false, data = result.data)
            }
        }
    }
    fun deletePet(id: Long){
        launch{
            val result = withContext(Dispatchers.IO){
                petsRemoteRepositoryImpl.deletePet(id)
            }
            when(result){
                is CommunicationResult.CommunicationError -> petDetailUIState.value = UiState(loading = false, data = null, errors = DetailErrors(
                    R.string.no_internet_connection)
                )
                is CommunicationResult.Error -> petDetailUIState.value = UiState(loading = false, data = null, errors = DetailErrors(
                    R.string.failed_to_load_the_list))
                is CommunicationResult.Exception -> petDetailUIState.value = UiState(loading = false, data = null, errors = DetailErrors(
                    R.string.unknown_error))
                is CommunicationResult.Success -> {}
            }
        }
    }


}