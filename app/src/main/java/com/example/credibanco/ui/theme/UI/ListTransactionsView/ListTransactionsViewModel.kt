package com.example.credibanco.ui.theme.UI.ListTransactionsView

import TransactionDatabase
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credibanco.ui.theme.Data.Model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class ListTransactionsViewModel(private val context: Context): ViewModel() {

    private val transactionDatabase = TransactionDatabase(context)

    private val _search = MutableStateFlow("")
    val search =  _search.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _recepId = MutableStateFlow(transactionDatabase.getAllTransactions())

    val searchRecepId = search.combine(_recepId) { text, recepId ->
        if (text.isBlank()) {
            recepId
        } else {
            recepId.filter { transaction ->
                transaction.receiptId != null && transaction.receiptId.contains(
                    text,
                    ignoreCase = true
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _recepId.value
    )

    fun getAllTransactions(): LiveData<List<Transaction>> {
        return transactionDatabase.getAllTransactionsLiveData()
    }


    fun onSearchTextChange(text: String) {
        _search.value = text
    }
}
