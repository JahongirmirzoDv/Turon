package com.example.turon.data.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.turon.App
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.Result
import com.example.turon.utils.SharedPref
import retrofit2.HttpException

class ReturnedDataSource (private var apiService: ApiService) : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key ?: START_PAGE_INDEX
        try {
            val userId= SharedPref(App.instance).getUserId()
            val response = apiService.getReturnedHistory(page,userId)
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
