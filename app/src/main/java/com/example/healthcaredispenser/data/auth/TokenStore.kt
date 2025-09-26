package com.example.healthcaredispenser.data.auth

import android.content.Context
import com.example.healthcaredispenser.data.local.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * JWT 토큰 저장/관리 (DataStore 기반)
 * - 앱 시작 시 [init]으로 초기화
 * - 로그인 성공 시 [set] 호출
 * - 로그아웃 시 [clear] 호출
 * - API 요청 시 [get]으로 현재 토큰 읽기
 */
object TokenStore {
    private lateinit var dataStore: TokenDataStore
    @Volatile private var cached: String? = null

    /** Application.onCreate()에서 호출 */
    fun init(context: Context) {
        dataStore = TokenDataStore(context)
        // 최초 실행 시 캐싱
        cached = runBlocking { dataStore.getToken().first() }
    }

    /** 토큰 저장 */
    fun set(token: String?) {
        cached = token
        runBlocking {
            dataStore.setToken(token)
        }
    }

    /** 현재 토큰 읽기 */
    fun get(): String? {
        if (cached == null) {
            cached = runBlocking { dataStore.getToken().first() }
        }
        return cached
    }

    /** 로그아웃/만료 시 호출 */
    fun clear() = set(null)
}
