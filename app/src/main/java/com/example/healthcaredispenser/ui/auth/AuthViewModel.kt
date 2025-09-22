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

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val res = repo.signUp(SignUpRequest(email, password))
                tokenStore.save(res.accessToken)
                _state.update { it.copy(loading = false, loggedIn = true) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message ?: "회원가입 실패") }
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
