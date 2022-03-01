package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.ActiveTurn
import com.example.turon.data.model.ClientData
import com.example.turon.data.model.Turn
import com.example.turon.data.model.TurnHistory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.*

class TurnAcceptRepository(private val apiHelper: ApiHelper) {

    suspend fun getTurnAccept(): UIState<List<Turn>> {
        try {
            val response = apiHelper.getTurnAccept()
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getTurnHistory(): UIState<TurnHistoryActInAct> {
        try {
            val response = apiHelper.getTurnHistory()
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }



    suspend fun getActiveTurn(): UIState<ActiveTurnResponse> {
        try {
            val response = apiHelper.getActiveTurn()
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }

    suspend fun addTurn(body: HashMap<String, Any>?): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.addTurn(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                } else {
                    UIState.Error("Bu raqam bilan navbatga olingan!")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
    suspend fun insertTurns(body: HashMap<String, Any>?): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.insertTurns(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                } else {
                    UIState.Error("Bu raqam bilan navbatga olingan!")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun carLeave(turnId: Int,status:Int): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.carLeave(turnId,status)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                } else {
                    UIState.Error("Bunday navbat yoq!")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getTurnClient(order_id: Int): UIState<List<ClientData>> {
        try {
            val response = apiHelper.getTurnClient(order_id)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.orderDetailsData)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}