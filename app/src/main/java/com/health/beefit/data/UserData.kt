package com.health.beefit.data



    data class UserData(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val email: String,
        val userName: String,
        val password: String,
        val earnedPoints: Number = 0,
        val rewards: MutableMap<String, Number> = mutableMapOf() // Initialize an empty map for rewards
    )
