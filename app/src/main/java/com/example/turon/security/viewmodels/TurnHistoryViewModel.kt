package com.example.turon.security.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.turon.data.api.ApiService
import com.example.turon.data.data.ProductionDataSource
import com.example.turon.data.data.TurnHistoryDataSource
import com.example.turon.data.data.TurnHistoryToDataSource
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.Turn
import com.example.turon.data.model.TurnHistory
import com.example.turon.data.model.repository.state.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TurnHistoryViewModel (private val apiService: ApiService) : ViewModel() {


    fun getTurnPagination(text:String): Flow<PagingData<TurnHistory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TurnHistoryDataSource(apiService,text) }
        ).flow
    }

    fun getTurnPaginationTo(text:String,from_date:String,to_date:String): Flow<PagingData<TurnHistory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TurnHistoryToDataSource(apiService,text,from_date,to_date) }
        ).flow
    }
}