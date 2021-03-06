package com.example.turon.production.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.turon.data.api.ApiService
import com.example.turon.data.data.*
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.ResultN
import com.example.turon.data.model.response.HistoryProResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AllHistoryViewModel(private val apiService: ApiService) : ViewModel() {
    var lv = MutableLiveData<HistoryProResponse>()
    fun getProductionPagination(): Flow<PagingData<HistoryProData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductionDataSource(apiService) }
        ).flow
    }

    fun getAcceptancePagination(userId:Int): Flow<PagingData<HistoryProData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AcceptanceDataSource(apiService,userId) }
        ).flow
    }

    fun getOrderPagination(
        text: String,
        userId: Int,
        from_date: String,
        to_date: String
    ): Flow<PagingData<ResultN>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { OrderHistoryDataSource(apiService, text, userId,from_date,to_date) }
        ).flow
    }

    fun getReturnedPagination(): Flow<PagingData<ResultN>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ReturnedDataSource(apiService) }
        ).flow
    }

    fun getReturnedProPagination(): Flow<PagingData<HistoryProData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ReturnedProDataSource(apiService) }
        ).flow
    }


    fun getHistoryProFilter(
        user_id: Int,
        date_start: String,
        date_end: String
    ): MutableLiveData<HistoryProResponse> {
        viewModelScope.launch {
            lv.value = apiService.getHistoryProFilter(user_id, date_start, date_end)
        }
        return lv
    }
}