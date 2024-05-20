package com.health.beefit.utils

import com.health.beefit.data.LoginRequest
import com.health.beefit.data.LoginResponse
import com.health.beefit.data.RegistrationResponse
import com.health.beefit.data.UpdatePoints
import com.health.beefit.data.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    // Update user by ID
    @PUT("/api/users/{id}")
    fun updateEarnedPoints(@Path("id") userId: String, @Body updatePoints: UpdatePoints): Call<UserData>
}
