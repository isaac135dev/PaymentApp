package com.example.credibanco.ui.theme.UI.TransactionScreen

import TransactionDatabase
import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credibanco.ui.theme.Core.PaymentApiClient
import com.example.credibanco.ui.theme.Data.Model.ResponseData
import com.example.credibanco.ui.theme.Data.Model.TransactionData
import com.example.credibanco.ui.theme.Data.Network.PaymentService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TransactionViewModel(private val context: Context) : ViewModel() {

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> = _amount

    private val _cardNumber = MutableLiveData<String>()
    val cardNumber: LiveData<String> = _cardNumber

    private val _enableButton = MutableLiveData<Boolean>()
    val enableButton: LiveData<Boolean> = _enableButton

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun PymentTransaction(currentAmount: String, currentCardNumber: String) {

        viewModelScope.launch {
            val commerceCode = "000123"
            val terminalCode = "000ABC"
            val transactionData = TransactionData(
                id = "123",
                commerceCode = "000123",
                terminalCode = "000ABC",
                amount = currentAmount, // Ejemplo: $100.00
                card = currentCardNumber
            )

            val credentials = "$commerceCode$terminalCode"
            val base64Credentials =
                Base64.encodeToString(credentials.encodeToByteArray(), Base64.NO_WRAP)
            val basicAuth = "Basic $base64Credentials"

            val retrofit = PaymentApiClient.postPaymentAuthorization()
            val service = retrofit.create(PaymentService::class.java)

            val response = service.authorizeTransaction(basicAuth, transactionData)


            val responseData = ResponseData(
                receiptId = response.receiptId,
                rrn = response.rrn,
                statusCode = response.statusCode,
                statusDescription = response.statusDescription,
            )
            val dbHelper = TransactionDatabase(context)
            dbHelper.insertTransaction(responseData)


            println("Respuesta de autorización: $response")
        }

    }

    fun onTransactionChange(amount: String, cardNumber: String) {
        _amount.value = amount
        _cardNumber.value = cardNumber
        _enableButton.value = isValidAmount(amount) && isValidCardNumber(cardNumber)
    }

    private fun isValidCardNumber(cardNumber: String): Boolean = cardNumber.length > 15

    private fun isValidAmount(amount: String): Boolean = amount.length > 3

    suspend fun onButtonSelected() {
        _isLoading.value = true
        val currentAmount = _amount.value
        val currentCardNumber = _cardNumber.value

        if (!currentAmount.isNullOrEmpty() && !currentCardNumber.isNullOrEmpty()) {
            // Llama a la función PymentTransaction con los valores actuales

            PymentTransaction(currentAmount, currentCardNumber)


        }
        delay(6000)
        _isLoading.value = false
    }
}