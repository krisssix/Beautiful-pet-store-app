package cz.mendelu.pef.petstore.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
 @Json(name = "id")
 val id: Long?,
 @Json(name = "username")
 val username: String?,
 @Json(name = "firstName")
 val firstName: String?,
 @Json(name = "lastName")
 val lastName: String?,
 @Json(name = "email")
 val email: String?,
 @Json(name = "password")
 val password: String?,
 @Json(name = "phone")
 val phone: String?,
 @Json(name = "userStatus")
 val userStatus: Int?
)
