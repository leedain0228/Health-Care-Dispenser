package com.example.healthcaredispenser.data.model.profile

/**
 * POST /api/profiles 요청 바디
 * - Swagger 예시 기준
 */
data class CreateProfileRequest(
    val name: String,
    val height: Double,
    val weight: Double,
    val gender: String,          // "MALE" | "FEMALE"
    val tags: List<String>,      // 예: ["ALCOHOL"]
    val conditions: List<String> // 예: ["PREGNANT"]
)

/**
 * 프로필 응답 DTO
 * - 서버 응답 구조
 * - Swagger 예시: { "id": 0, "name": "string" }
 * - 다른 필드가 올 수도 있으니 Nullable + 기본값 처리
 */
data class ProfileDto(
    val id: Long? = null,
    val name: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val gender: String? = null,
    val tags: List<String>? = null,
    val conditions: List<String>? = null
)

/** ✅ GET /api/profiles 응답 래퍼 */
data class ProfileResponse(
    val items: List<ProfileDto> = emptyList(),
    val count: Int = 0
)