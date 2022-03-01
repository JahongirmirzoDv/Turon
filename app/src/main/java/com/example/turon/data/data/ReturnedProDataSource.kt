package com.example.turon.data.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.turon.App
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.HistoryProData
import com.example.turon.utils.SharedPref
import retrofit2.HttpException

class ReturnedProDataSource (private var apiService: ApiService) : PagingSource<Int, HistoryProData>() {
    override fun getRefreshKey(state: PagingState<Int, HistoryProData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryProData> {
        val page = params.key ?: START_PAGE_INDEX
        try {
            val userId= SharedPref(App.instance).getUserId()
            val response = apiService.getReturnedProHistory(page,userId)
            val nextKey=if(response.next.isNullOrEmpty()){
                null
            }else{
                page+1
            }
            return LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val START_PAGE_INDEX = 1
    }
}
