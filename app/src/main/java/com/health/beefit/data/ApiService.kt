package com.health.beefit.data

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/api/users")
    fun signUp(@Body registrationData: RegistrationData): Call<RegistrationResponse>

//    @POST("login")
//    fun login(@Body loginData: LoginData): Call<LoginResponse>
//
//    @PUT("users/{id}")
//    fun updateUser(@Path("id") userId: String, @Body updateData: UpdateUserData): Call<UserResponse>
//
}
