package com.example.turon.data.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.OrderHistory
import retrofit2.HttpException

class OrderHistoryDataSource (private var apiService: ApiService,private var text:String,private var userId:Int) : PagingSource<Int, OrderHistory>() {
    override fun getRefreshKey(state: PagingState<Int, OrderHistory>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderHistory> {
        val page = params.key ?: START_PAGE_INDEX
        try {
            val response = apiService.getOrderHistory(page,userId,text)
            Log.e("test", "load: ${response.results.toString()}  ${response.results.size}", )
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