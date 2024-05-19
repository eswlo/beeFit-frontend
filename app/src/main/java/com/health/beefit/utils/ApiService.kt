package com.health.beefit.utils

import com.health.beefit.data.LoginRequest
import com.health.beefit.data.LoginResponse
import com.health.beefit.data.RegistrationResponse
import com.health.beefit.data.UserData
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // call user sing up
    @POST("/api/users")
    fun signUp(@Body userData: UserData): Call<RegistrationResponse>

    // call user login
    @POST("/api/users/login")
    fun logIn(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Get one user by ID
    @GET("/api/users/{id}")
    fun getOneUserById(@Path("id") id: String): Call<UserData>

//    @PUT("users/{id}")
//    fun updateUser(@Path("id") userId: String, @Body updateData: UpdateUserData): Call<UserResponse>
//
}
