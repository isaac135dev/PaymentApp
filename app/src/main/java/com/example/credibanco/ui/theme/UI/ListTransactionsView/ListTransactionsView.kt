package com.example.credibanco.ui.theme.UI.ListTransactionsView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.credibanco.R
import com.example.credibanco.ui.theme.Components.ItemListTransaction

@Composable
fun ListTransactionsView(
    viewModel: ListTransactionsViewModel,
    navController: NavController,
    navigateToView: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_200))

    ) {
        ContentView(navController)
        ModalContent(viewModel, navController)
    }
}

@Composable
fun ContentView(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {

        Column(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 18.dp),
            horizontalAlignment = Alignment.Start

        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_keyboard_arrow_left),
                contentDescription = "like",
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = { navController.navigateUp() })
            )

            Text(
                text = "Lists Transactions",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

    }
}

@Composable
fun ModalContent(viewModel: ListTransactionsViewModel, navController: NavController) {
    val transactions by viewModel.getAllTransactions().observeAsState(emptyList())

    val search by viewModel.search.collectAsState()
    val receptId by viewModel.searchRecepId.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White,
                MaterialTheme.shapes.extraLarge.copy(
                    bottomEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(0.dp),
                )
            )
            .padding(bottom = 100.dp)
    ) {


        Column {
            searchField(search, viewModel::onSearchTextChange)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(receptId) { transaction ->
                    ItemListTransaction(
                        name = transaction.receiptId ?: "",
                        Status = transaction.statusDescription ?: "",
                        onClick = {
                            transaction.receiptId?.let {
                                transaction.statusDescription?.let { it1 ->
                                    transaction.rrn?.let { it2 ->
                                        navigateToDetail(
                                            navController,
                                            it,
                                            it1,
                                            it2
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun searchField(search: String, onSearchTextChanged: (String) -> Unit) {
    TextField(
        value = search,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
        onValueChange =  onSearchTextChanged,
        label = { Text("buscar transaccion") },
        singleLine = true,
        maxLines = 1
    )
}

private fun navigateToDetail(
    navController: NavController,
    receiptId: String,
    statusDescription: String,
    rrn: String
) {
    // Navegar al detalle del elemento pasando el ID del elemento como argumento
    navController.navigate("ItemListDetailView/${receiptId}/${statusDescription}/${rrn}")
}
