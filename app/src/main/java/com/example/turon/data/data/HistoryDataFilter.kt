package com.example.turon.data.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.turon.App
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.HistoryProData
import com.example.turon.utils.SharedPref
import retrofit2.HttpException

class HistoryDataFilter(private var apiService: ApiService) : PagingSource<String, HistoryProData>() {
    override fun getRefreshKey(state: PagingState<String, HistoryProData>): String? {
        return null
    }

    override suspend fun load(params: PagingSource.LoadParams<String>): PagingSource.LoadResult<String, HistoryProData> {
        val start_date = params.key?.substring(0,12)
        val end_date = params.key?.substring(13, params.key!!.length)
        Log.e("qales", "load: ${start_date} : $end_date", )
        val userId= SharedPref(App.instance).getUserId()
        try {
            val response = apiService.getHistoryProFilter(userId, start_date!!, end_date!!)
            val nextKey=if(response.next.isNullOrEmpty()){
                null
            }else{
                "1"
            }
            return PagingSource.LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            return PagingSource.LoadResult.Error(e)
        } catch (e: HttpException) {
            return PagingSource.LoadResult.Error(e)
        }
    }

    companion object {
        const val START_PAGE_INDEX = 1
    }
}