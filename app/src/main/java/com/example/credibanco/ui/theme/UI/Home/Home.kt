package com.example.credibanco.ui.theme.UI.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.credibanco.R
import com.example.credibanco.ui.theme.Components.ItemListTransaction

@Composable
fun Home(viewModel: HomeViewModel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ContentTop()
        SectionHeader(text = "Popular Categories")
        MyCard(navController)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SectionHeader(text = "Latest Transactions")
            moreAllItems(navController)
        }
        TransactionList(viewModel, navController)
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier.padding(bottom = 15.dp, top = 30.dp, start = 10.dp)
    )
}

@Composable
fun moreAllItems(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.ic_more_horiz),
        contentDescription = "more",
        modifier = Modifier
            .size(50.dp)
            .padding(top = 10.dp)
            .clickable(onClick = {
                navigateToDetailList(navController)
            })
    )
}


@Composable
fun ContentTop() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.purple_200),
                MaterialTheme.shapes.extraLarge.copy(
                    topEnd = CornerSize(0.dp),
                    topStart = CornerSize(0.dp)
                )
            )
            .padding(bottom = 100.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 18.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_account_circle),
                contentDescription = "like",
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = {})
            )

            Text(
                text = "My card",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCard(onNavigateClick: NavController) {
    Card(
        onClick = {
            onNavigateClick.navigate("AuthorizationScreen")
            println("Boton precionado 1")
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .height(120.dp)
            .width(180.dp)
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_receipt_long),
                contentDescription = "like",
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 10.dp)
            )
            Text(
                text = "Food",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun TransactionList(viewModel: HomeViewModel, navController: NavController) {
    val transactions by viewModel.getAllTransactions().observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(transactions) { transaction ->
            ItemListTransaction(
                name = transaction.receiptId ?: "",
                Status = transaction.statusDescription ?: "",
                amount = transaction.amount ?: "",
                onClick = {
                    transaction.receiptId?.let {
                        transaction.statusDescription?.let { it1 ->
                            transaction.rrn?.let { it2 ->
                                transaction.amount?.let { it3 ->
                                    navigateToDetail(
                                        navController,
                                        it,
                                        it1,
                                        it2,
                                        it3
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

private fun navigateToDetail(
    navController: NavController,
    receiptId: String,
    statusDescription: String,
    rrn: String,
    amount: String
) {
    // Navegar al detalle del elemento pasando el ID del elemento como argumento
    navController.navigate("ItemListDetailView/${receiptId}/${statusDescription}/${rrn}/${amount}")
}

private fun navigateToDetailList(navController: NavController) {

    navController.navigate("ListTransactionsView")
}