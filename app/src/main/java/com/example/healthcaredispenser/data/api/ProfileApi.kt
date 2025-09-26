package com.example.healthcaredispenser.data.api

import com.example.healthcaredispenser.data.model.profile.CreateProfileRequest
import com.example.healthcaredispenser.data.model.profile.ProfileDto
import com.example.healthcaredispenser.data.model.profile.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {
    @POST("api/profiles")
    suspend fun create(
        @Body req: CreateProfileRequest
    ): ProfileDto

    /** ✅ 배열이 아니라 래퍼로 받기 */
    @GET("api/profiles")
    suspend fun list(): ProfileResponse
}

fun provideProfileApi(): ProfileApi =
    RetrofitClient.retrofit.create(ProfileApi::class.java)
