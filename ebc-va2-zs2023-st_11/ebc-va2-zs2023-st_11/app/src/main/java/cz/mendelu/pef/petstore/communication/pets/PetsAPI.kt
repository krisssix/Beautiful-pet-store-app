package cz.mendelu.pef.petstore.communication.pets.pets

import cz.mendelu.pef.petstore.model.Pet
import cz.mendelu.pef.petstore.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {

    @Headers("Content-Type: application/json")
    @GET("pet/findByStatus")
    suspend fun getAllPets(@Query("status") status: String): Response<List<Pet>>

    @Headers("Content-Type: application/json")
    @GET("pet/{id}")
    suspend fun findById(@Path("id") id: Long): Response<Pet>

    @Headers("Content-Type: application/json")
    @DELETE("pet/{id}")
    suspend fun deletePet(@Path("id") id: Long): Response<Void>

    @Headers("Content-Type: application/json")
    @POST("pet")
    suspend fun addPet(@Body pet: Pet): Response<Pet>

    @Headers("Content-Type: application/json")
    @PUT("pet")
    suspend fun editPet(@Body pet: Pet): Response<Pet>


}