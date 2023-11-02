package cz.mendelu.pef.petstore.communication.pets.pets

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val petsAPI: PetsAPI)
    : IPetsRemoteRepository {

    override suspend fun getAllPets(status: String): CommunicationResult<List<Pet>> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.getAllPets(status)
            }
        )
    }

    override suspend fun findById(id: Long): CommunicationResult<Pet> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.findById(id)
            }
        )
    }

    override suspend fun deletePet(id: Long): CommunicationResult<Void> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.deletePet(id)
            }
        )
    }

    override suspend fun addPet(pet: Pet): CommunicationResult<Pet> {
        return processResponse(
            withContext(Dispatchers.IO){
                petsAPI.addPet(pet)
            }
        )
    }

    override suspend fun editPet(pet: Pet): CommunicationResult<Pet> {
        return processResponse(
            withContext(Dispatchers.IO){
                petsAPI.editPet(pet)
            }
        )
    }
}