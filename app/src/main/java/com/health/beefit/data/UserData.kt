package com.health.beefit.data
data class UserData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val userName: String,
    val password: String,
    val earnedPoints: Number? = null // Nullable Number with default value null
)
