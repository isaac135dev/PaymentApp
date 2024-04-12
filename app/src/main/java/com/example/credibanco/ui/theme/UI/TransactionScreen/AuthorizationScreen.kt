package com.example.credibanco.ui.theme.UI.TransactionScreen


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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.credibanco.R
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AuthorizationScreen(viewModel: TransactionViewModel, navController: NavController, onClickToNavigate: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_200))

    ) {
        ContentView(onClickToNavigate)
        ModalContent(viewModel, navController)
    }
}

@Composable
fun ContentView(onClickToNavigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp)
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
                    .clickable(onClick = onClickToNavigate)
            )

            Text(
                text = "Transaction",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

    }
}

@Composable
fun ModalContent(viewModel: TransactionViewModel, navController: NavController) {
    val amount: String by viewModel.amount.observeAsState(initial = "")
    val cardNumber: String by viewModel.cardNumber.observeAsState(initial = "")
    val enableButton: Boolean by viewModel.enableButton.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isLoadingAtivation: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val corrutineScope = rememberCoroutineScope()

    if(isLoading) {
        Box (
            modifier = Modifier.background(Color.Transparent)
        ){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

    if(isLoadingAtivation){
        AlertDialogExample(
            dialogTitle = "Estado de Transaccion",
            dialogText = "Transaccion Aprobada",
            icon = Icons.Filled.CheckCircle
        )
        navController.navigateUp()
    }

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


        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AmountField(amount) { viewModel.onTransactionChange(it, cardNumber) }
            CardNumberField(cardNumber) { viewModel.onTransactionChange(amount, it) }
            TransactionButton(enableButton) {
                corrutineScope.launch {
                    viewModel.onButtonSelected()

                }
            }
        }

    }
}

@Composable
fun AmountField(amount: String, onAuthorizeClicked: (String) -> Unit) {
    val text = remember { mutableStateOf(TextFieldValue("")) }
    val formattedSearch = formatMoneyValue(amount)

    TextField(
        value = text.value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
        onValueChange = { newValue ->
            text.value = newValue
            onAuthorizeClicked(newValue.text)
        },
        label = { Text("Monto de la transacción") },
        singleLine = true,
        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(textDecoration = TextDecoration.None),
        leadingIcon = { Text(text = formattedSearch, color = Color.Black, fontSize = 16.sp, modifier = Modifier.padding(start = 6.dp)) },
    )
}


fun formatMoneyValue(value: String): String {
    val plainValue = value.replace(Regex("[^\\d]"), "")
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val amount = plainValue.toBigDecimalOrNull() ?: BigDecimal.ZERO
    return numberFormat.format(amount.divide(BigDecimal(100)))
}

@Composable
fun CardNumberField(cardNumber: String, onAuthorizeClicked: (String) -> Unit) {
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = cardNumber,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
        onValueChange = { onAuthorizeClicked(it) },
        label = { Text("Número de tarjeta") },
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun TransactionButton(enableButton: Boolean, onSelected: () -> Unit) {
    Button(
        onClick = { onSelected() },
        enabled = enableButton,
        modifier = Modifier.padding(horizontal = 50.dp)
    ) {
        Text("Autorizar Transacción")

    }
}


@Composable
fun AlertDialogExample(
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
        },
        confirmButton = {
        },
        dismissButton = {
        }
    )
}