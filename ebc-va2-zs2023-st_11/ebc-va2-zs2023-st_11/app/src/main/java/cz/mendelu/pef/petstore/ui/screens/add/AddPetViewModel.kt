package cz.mendelu.pef.petstore.ui.screens.add

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.petstore.R
import cz.mendelu.pef.petstore.architecture.BaseViewModel
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petsRemoteRepositoryImpl: PetsRemoteRepositoryImpl
) : BaseViewModel() {


    val addPetUIState: MutableState<UiState<Pet, AddPetErrors>> = mutableStateOf(UiState())

    fun addPet(pet: Pet){
        launch{
            val result = withContext(Dispatchers.IO){
                petsRemoteRepositoryImpl.addPet(pet)
            }
            when(result){
                is CommunicationResult.CommunicationError -> addPetUIState.value = UiState(loading = false, data = null, errors = AddPetErrors(
                    R.string.no_internet_connection)
                )
                is CommunicationResult.Error -> addPetUIState.value = UiState(loading = false, data = null, errors = AddPetErrors(
                    R.string.failed_to_load_the_list))
                is CommunicationResult.Exception -> addPetUIState.value = UiState(loading = false, data = null, errors = AddPetErrors(
                    R.string.unknown_error))
                is CommunicationResult.Success -> addPetUIState.value = UiState(loading = false, data = result.data)
            }
        }
    }
}