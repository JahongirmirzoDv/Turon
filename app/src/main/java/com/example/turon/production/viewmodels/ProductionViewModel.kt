package com.example.turon.production.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turon.data.model.*
import com.example.turon.data.model.repository.FeedAcceptanceRepository
import com.example.turon.data.model.repository.ProductionRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductionViewModel(private val repository: ProductionRepository) : ViewModel() {

private val _productState = MutableStateFlow<UIState<List<ProductPro>>>(UIState.Loading)
    val productState: StateFlow<UIState<List<ProductPro>>> = _productState
    suspend fun getProductPro(user_id:Int) {
        _productState.value = repository.getProductPro(user_id)
    }

private val _cargoState = MutableStateFlow<UIState<List<TegirmonData>>>(UIState.Loading)
    val cargoState: StateFlow<UIState<List<TegirmonData>>> = _cargoState
    suspend fun getCargoMan() {
        _cargoState.value = repository.getCargoMan()
    }


private val _prBaskedState = MutableStateFlow<UIState<List<LoadOrder>>>(UIState.Loading)
    val prBaskedState: StateFlow<UIState<List<LoadOrder>>> = _prBaskedState
    suspend fun getLoadOrder(user_id: Int, order_id: Int) {
        _prBaskedState.value = repository.getLoadOrder(user_id,order_id)
    }


    private val _postItemState = MutableStateFlow<UIState<ProAcceptResponse>>(UIState.Loading)
    val postItemState: StateFlow<UIState<ProAcceptResponse>> = _postItemState
    suspend fun postItemPro(code: RequestProC) {
        _postItemState.value = repository.postItemPro(code)
    }

    private val _postReturnItemState = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val postReturnItemState: StateFlow<UIState<EditStoreResponse>> = _postReturnItemState
    suspend fun postReturnProduct(code: RequestReturnProduct) {
        _postReturnItemState.value = repository.postReturnProduct(code)
    }

    private val _loadOrderState = MutableStateFlow<UIState<LoadOrderBaskedResponse>>(UIState.Loading)
    val loadOrderState: StateFlow<UIState<LoadOrderBaskedResponse>> = _loadOrderState
    suspend fun postLoadOrder(code: RequestPro) {
        _loadOrderState.value = repository.postLoadOrder(code)
    }

    private val _addCargoState = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val addCargoState: StateFlow<UIState<EditStoreResponse>> = _addCargoState
    suspend fun addCargoToBasket(bask_id:Int,brigada:Int) {
        _addCargoState.value = repository.addCargoToBasket(bask_id,brigada)
    }


}