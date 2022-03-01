package com.example.turon.data.model.response

import com.example.turon.data.model.Providers
import com.example.turon.data.model.repository.state.UIState

data class ProvidersResponse(
    var success: Boolean,
    var clients:List<TegirmonData>
)
