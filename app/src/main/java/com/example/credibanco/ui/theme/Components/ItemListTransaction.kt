package com.example.credibanco.ui.theme.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ItemListTransaction(name: String, Status: String, amount: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
        ) {
            Text(
                text = name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Text(
                text = Status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }
        Text(
            text = "- ${formatMoneyValue(amount)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
}

fun formatMoneyValue(value: String): String {
    val plainValue = value.replace(Regex("[^\\d]"), "")
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val amount = plainValue.toBigDecimalOrNull() ?: BigDecimal.ZERO
    return numberFormat.format(amount.divide(BigDecimal(100)))
}