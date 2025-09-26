package com.example.healthcaredispenser.data.api

import com.example.healthcaredispenser.data.model.profile.CreateProfileRequest
import com.example.healthcaredispenser.data.model.profile.ProfileItem
import com.example.healthcaredispenser.data.model.profile.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {
    /** ✅ 생성 시 {id, name}만 내려옴 → ProfileItem으로 받기 */
    @POST("api/profiles")
    suspend fun create(
        @Body req: CreateProfileRequest
    ): ProfileItem

    /** ✅ 목록 조회 시 래퍼 구조 */
    @GET("api/profiles")
    suspend fun list(): ProfileResponse
}

fun provideProfileApi(): ProfileApi =
    RetrofitClient.retrofit.create(ProfileApi::class.java)
