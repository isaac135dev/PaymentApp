package com.example.credibanco.ui.theme.UI.ListTransactionsView

import TransactionDatabase
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.credibanco.ui.theme.Data.Model.Transaction

class ListTransactionsViewModel(private val context: Context): ViewModel() {
    private val transactionDatabase = TransactionDatabase(context)

    // Método para obtener todas las transacciones
    fun getAllTransactions(): LiveData<List<Transaction>> {
        return transactionDatabase.getAllTransactionsLiveData()
    }
}