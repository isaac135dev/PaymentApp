package com.example.credibanco.ui.theme.Data.Model

import com.google.gson.annotations.SerializedName

data class TransactionData(
    @SerializedName("id") val id: String,
    @SerializedName("commerceCode") val commerceCode: String,
    @SerializedName("terminalCode") val terminalCode: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("card") val card: String
)

data class CancellationData(
    @SerializedName("receiptId") val receiptId: String,
    @SerializedName("rrn") val rrn: String
)

data class ResponseData(
    @SerializedName("receiptId") val receiptId: String,
    @SerializedName("rrn") val rrn: String,
    @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusDescription") val statusDescription: String
)

data class Transaction(
    @SerializedName("receiptId") val receiptId: String?,
    @SerializedName("rrn") val rrn: String?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("statusDescription") val statusDescription: String?
){
    fun searchQuery(receiptId: String): Boolean {
        val combinations = listOf("${receiptId.first()}")

        return combinations.any { it.contains(receiptId, ignoreCase = true) }
    }
}
