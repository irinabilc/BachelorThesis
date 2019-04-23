package irinabilc.bachelorthesis.networking

import irinabilc.bachelorthesis.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiNetworkInterface {

    @POST ("users")
    fun addUser(@Body user: User) : Call<User>
}