package cz.mendelu.pef.petstore.communication.users

import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String)
    : Response<User>
}