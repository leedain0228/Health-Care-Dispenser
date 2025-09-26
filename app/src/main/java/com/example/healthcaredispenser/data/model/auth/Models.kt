package com.example.healthcaredispenser.data.model.auth

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName(value = "token", alternate = ["accessToken", "access_token"])
    val token: String,
    val id: Long? = null,
    val email: String? = null
)
