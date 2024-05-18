package com.health.beefit.data

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/api/users")
    fun signUp(@Body userData: UserData): Call<RegistrationResponse>

    @POST("/api/users/login")
    fun logIn(@Body loginRequest: LoginRequest): Call<LoginResponse>

//    @PUT("users/{id}")
//    fun updateUser(@Path("id") userId: String, @Body updateData: UpdateUserData): Call<UserResponse>
//
}
