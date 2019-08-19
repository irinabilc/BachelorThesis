package irinabilc.bachelorthesis.networking

import irinabilc.bachelorthesis.domain.Image
import irinabilc.bachelorthesis.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

interface ApiNetworkInterface {
    @POST("login")
    fun confirmUser(@Body user: User) : Call<Boolean>

    @POST ("users")
    fun addUser(@Body user: User) : Call<User>

    @GET("users")
    fun getUsers() : Call<Array<User>>

    @POST("images")
    fun addImage(@Body image: Image) : Call<Boolean>
}