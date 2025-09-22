package com.example.healthcaredispenser.data.repository

import com.example.healthcaredispenser.data.api.AuthApi
import com.example.healthcaredispenser.data.model.auth.AuthResponse
import com.example.healthcaredispenser.data.model.auth.LoginRequest
import com.example.healthcaredispenser.data.model.auth.SignUpRequest

class AuthRepository(private val api: AuthApi) {
    suspend fun signUp(req: SignUpRequest): AuthResponse = api.signUp(req)
    suspend fun login(req: LoginRequest): AuthResponse = api.login(req)
}
