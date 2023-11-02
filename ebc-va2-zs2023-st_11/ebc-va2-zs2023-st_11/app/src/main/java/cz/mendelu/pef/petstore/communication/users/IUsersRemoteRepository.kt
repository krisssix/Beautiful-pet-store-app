package cz.mendelu.pef.petstore.communication.pets.users

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.architecture.IBaseRemoteRepository
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.User

interface IUsersRemoteRepository : IBaseRemoteRepository {
    suspend fun login(username: String, password: String): CommunicationResult<User>
}