package com.example.credibanco.ui.theme.UI.ItemListDetail

import TransactionDatabase
import android.content.Context
import android.util.Base64
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credibanco.ui.theme.Core.PaymentApiClient
import com.example.credibanco.ui.theme.Data.Model.CancellationData
import com.example.credibanco.ui.theme.Data.Model.ResponseData
import com.example.credibanco.ui.theme.Data.Network.PaymentService
import kotlinx.coroutines.launch

class ItemListDetaiViewModel(private val context: Context): ViewModel() {

    private fun AnnulmentTransaction(receiptId: String, rrn: String) {
        viewModelScope.launch {
            val commerceCode = "000123"
            val terminalCode = "000ABC"

            val transactionData = CancellationData(
                receiptId = receiptId,
                rrn = rrn
            )

            val credentials = "$commerceCode$terminalCode"
            val base64Credentials =
                Base64.encodeToString(credentials.encodeToByteArray(), Base64.NO_WRAP)
            val basicAuth = "Basic $base64Credentials"

            val retrofit = PaymentApiClient.postPaymentAuthorization()
            val service = retrofit.create(PaymentService::class.java)

            val response = service.cancelTransaction(basicAuth, transactionData)


            val responseData = ResponseData(
                receiptId = response.receiptId ?: "Anulada",
                rrn = response.rrn ?: "Anulada",
                statusCode = response.statusCode,
                statusDescription = response.statusDescription,
            )

            val dbHelper = TransactionDatabase(context)
            dbHelper.updateTransaction(responseData)

            println("Respuesta de mi base de datos: ${responseData}")
            println("Respuesta de autorización: $response")
        }

    }

    fun onButtonSelected(receiptId: String, rrn: String) {

        AnnulmentTransaction(receiptId, rrn)
    }
}

