package com.example.healthcaredispenser.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcaredispenser.data.model.profile.CreateProfileRequest
import com.example.healthcaredispenser.data.model.profile.ProfileDto
import com.example.healthcaredispenser.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class ProfileUiState(
    val loading: Boolean = false,
    val profiles: List<ProfileDto> = emptyList(),
    val saving: Boolean = false,
    val saved: Boolean = false,
    val unauthorized: Boolean = false,     // 401 처리용
    val error: String? = null
)

class ProfileViewModel(
    private val repo: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui.asStateFlow()

    /** 목록 불러오기 */
    fun fetch() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, error = null, unauthorized = false)

            repo.fetchProfiles()
                .onSuccess { list ->
                    _ui.value = _ui.value.copy(
                        loading = false,
                        profiles = list,
                        error = null,
                        unauthorized = false
                    )
                }
                .onFailure { e ->
                    Log.e("ProfileViewModel", "fetch error", e)
                    _ui.value = _ui.value.copy(
                        loading = false,
                        error = humanReadable(e),
                        unauthorized = isUnauthorized(e)
                    )
                }
        }
    }

    /** 다시 시도 버튼용 */
    fun retryFetch() = fetch()

    /**
     * 프로필 생성
     * - 성공 시 saved=true 로 플래그 올리고, 목록에도 추가
     */
    fun create(
        req: CreateProfileRequest,
        afterSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(
                saving = true,
                error = null,
                saved = false,
                unauthorized = false
            )

            repo.createProfile(req)
                .onSuccess { created ->
                    _ui.value = _ui.value.copy(
                        saving = false,
                        saved = true,
                        error = null,
                        unauthorized = false,
                        profiles = _ui.value.profiles + created
                    )
                    afterSuccess?.invoke()
                }
                .onFailure { e ->
                    Log.e("ProfileViewModel", "create error", e)
                    _ui.value = _ui.value.copy(
                        saving = false,
                        saved = false,
                        error = humanReadable(e),
                        unauthorized = isUnauthorized(e)
                    )
                }
        }
    }

    /** 저장 성공 플래그 소비 */
    fun clearSavedFlag() {
        _ui.value = _ui.value.copy(saved = false)
    }

    /** 에러 메시지 변환 */
    private fun humanReadable(e: Throwable): String = when (e) {
        is HttpException -> {
            when (e.code()) {
                401 -> "로그인이 필요합니다. 다시 로그인해 주세요."
                403 -> "접근 권한이 없습니다."
                404 -> "리소스를 찾을 수 없습니다."
                500 -> "서버 오류가 발생했습니다."
                else -> "요청 실패 (HTTP ${e.code()})"
            }
        }
        is IOException -> "네트워크 연결을 확인해 주세요."
        else -> e.message ?: "알 수 없는 오류가 발생했습니다."
    }

    private fun isUnauthorized(e: Throwable): Boolean =
        (e as? HttpException)?.code() == 401
}
