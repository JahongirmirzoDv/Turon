package com.example.turon.data.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.ResultN
import retrofit2.HttpException

class OrderHistoryDataSource (private var apiService: ApiService,private var text:String,private var userId:Int,var from_date:String,var to_date:String) : PagingSource<Int, ResultN>() {
    override fun getRefreshKey(state: PagingState<Int, ResultN>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultN> {
        val page = params.key ?: START_PAGE_INDEX
        try {
            val response = apiService.getOrderHistory(page,userId,text,from_date,to_date)
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