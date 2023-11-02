package cz.mendelu.pef.petstore.communication.pets.pets

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.architecture.IBaseRemoteRepository
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query

interface IPetsRemoteRepository : IBaseRemoteRepository {
    suspend fun getAllPets(status: String): CommunicationResult<List<Pet>>
    suspend fun findById(id: Long): CommunicationResult<Pet>
    suspend fun deletePet(id: Long): CommunicationResult<Void>
    suspend fun addPet(pet: Pet): CommunicationResult<Pet>
    suspend fun editPet(pet: Pet): CommunicationResult<Pet>

}