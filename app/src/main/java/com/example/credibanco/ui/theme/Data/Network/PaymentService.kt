package com.example.credibanco.ui.theme.Data.Network

import com.example.credibanco.ui.theme.Data.Model.CancellationData
import com.example.credibanco.ui.theme.Data.Model.ResponseData
import com.example.credibanco.ui.theme.Data.Model.Transaction
import com.example.credibanco.ui.theme.Data.Model.TransactionData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentService {

    @POST("authorization")
    suspend fun authorizeTransaction(
        @Header("Authorization") basicAuth: String,
        @Body transactionData: TransactionData
    ): ResponseData

    @POST("annulment")
    suspend fun cancelTransaction(
        @Header("Authorization") basicAuth: String,
        @Body cancellationData: CancellationData
    ): ResponseData

    @GET("search/{receiptId}")
    suspend fun searchTransaction(@Path("receiptId") receiptId: String): ResponseData

}