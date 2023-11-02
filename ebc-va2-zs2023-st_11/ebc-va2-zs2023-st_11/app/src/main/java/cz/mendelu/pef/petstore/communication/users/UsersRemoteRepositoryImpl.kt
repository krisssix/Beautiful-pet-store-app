package cz.mendelu.pef.petstore.communication.users

import android.util.Log
import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.users.IUsersRemoteRepository
import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(private val usersApi: UsersApi)
    : IUsersRemoteRepository {

    override suspend fun login(username: String, password: String): CommunicationResult<User> {
        try {
            val response = withContext(Dispatchers.IO) {
                usersApi.login(username, password)
            }
            Log.d("LoginResponse", "Response code: ${response.code()}, Response body: ${response.body()}, Response error body: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return CommunicationResult.Success(responseBody)
                } else {
                    return CommunicationResult.Error(
                        cz.mendelu.pef.petstore.architecture.Error(
                            response.code(),
                            response.errorBody()?.string() ?: "Unknown error"
                        )
                    )
                }
            } else {
                return CommunicationResult.Error(
                    cz.mendelu.pef.petstore.architecture.Error(
                        response.code(),
                        response.errorBody()?.string() ?: "Unknown error"
                    )
                )
            }
        } catch (ex: Exception) {
            Log.e("LoginException", ex.message, ex)
            return CommunicationResult.Exception(ex)
        }
    }

}


/*
override suspend fun login(username: String, password: String): CommunicationResult<User> {
    return processResponse(
        withContext(Dispatchers.IO) {
            usersApi.login(username, password)
        }
    )
}


 */


