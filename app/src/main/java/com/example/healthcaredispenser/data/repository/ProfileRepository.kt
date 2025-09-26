package com.example.healthcaredispenser.data.repository

import com.example.healthcaredispenser.data.api.ProfileApi
import com.example.healthcaredispenser.data.api.provideProfileApi
import com.example.healthcaredispenser.data.model.profile.CreateProfileRequest
import com.example.healthcaredispenser.data.model.profile.ProfileDto
import com.example.healthcaredispenser.data.model.profile.ProfileItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Profile 데이터 관리
 * - API 통신 캡슐화
 * - suspend fun → runCatching 으로 Result 반환하면 ViewModel에서 안전하게 사용 가능
 */
class ProfileRepository(
    private val api: ProfileApi = provideProfileApi()
) {
    /** ✅ 생성은 {id, name}만 내려오므로 ProfileItem으로 받는다 */
    suspend fun createProfile(req: CreateProfileRequest): Result<ProfileItem> =
        runCatching {
            withContext(Dispatchers.IO) { api.create(req) }
        }

    /** ✅ 래퍼 받아서 .items 로 변환 (목록/조회는 ProfileDto 사용) */
    suspend fun fetchProfiles(): Result<List<ProfileDto>> =
        runCatching {
            withContext(Dispatchers.IO) { api.list().items }
        }
}
