package com.example.healthcaredispenser.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcaredispenser.data.api.provideAuthApi
import com.example.healthcaredispenser.data.local.TokenDataStore
import com.example.healthcaredispenser.data.model.auth.LoginRequest
import com.example.healthcaredispenser.data.model.auth.SignUpRequest
import com.example.healthcaredispenser.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false
)

// AndroidViewModel을 쓰면 context(application) 접근 가능
class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val tokenStore = TokenDataStore(app)
    private val repo = AuthRepository(provideAuthApi())

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun signUp(email: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val res = repo.signUp(SignUpRequest(email, password, passwordConfirm)) // ✅ confirm 전송
                tokenStore.save(res.accessToken)
                _state.update { it.copy(loading = false, loggedIn = true) }
            } catch (e: Exception) {
                val msg = when (e) {
                    is retrofit2.HttpException -> {
                        val code = e.code()
                        if (code == 403) "서버 보안 정책 또는 유효성 검증으로 거절(403). passwordConfirm 포함해 다시 시도."
                        else "요청 실패($code)"
                    }
                    is java.io.IOException -> "네트워크 문제"
                    else -> e.message ?: "알 수 없는 오류"
                }
                _state.update { it.copy(loading = false, error = msg) }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val res = repo.login(LoginRequest(email, password))
                tokenStore.save(res.accessToken)
                _state.update { it.copy(loading = false, loggedIn = true) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message ?: "로그인 실패") }
            }
        }
    }
}
