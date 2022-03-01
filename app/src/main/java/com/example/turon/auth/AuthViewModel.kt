package com.example.turon.auth

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.repository.AuthRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.Data
import com.example.turon.data.model.response.OrderDetailsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<UIState<Data>>(UIState.Loading)
    val loginState: StateFlow<UIState<Data>> = _loginState
    suspend fun login(body: HashMap<String, Any>?) {
        _loginState.value = repository.login(body)
    }

}