package com.example.healthcaredispenser.data.network

import com.example.healthcaredispenser.data.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 모든 요청에 Authorization 헤더 자동 추가
 * - TokenStore.get() 값이 있으면 "Bearer <token>" 붙여줌
 * - 없으면 헤더 없이 보냄
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = TokenStore.get()
        android.util.Log.d("AuthInt", "Bearer? ${!token.isNullOrBlank()} ${token?.take(16)}...")

        val newReq = if (!token.isNullOrBlank()) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .build()
        } else {
            original.newBuilder()
                .header("Accept", "application/json")
                .build()
        }

        return chain.proceed(newReq)
    }
}
