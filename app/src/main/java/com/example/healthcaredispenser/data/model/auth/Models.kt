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

/** 서버가 token / access_token 등으로 줄 수 있으니 alternate만 유지 */
data class AuthResponse(
    @SerializedName(value = "accessToken", alternate = ["access_token", "token"])
    val accessToken: String
)
