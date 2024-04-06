package com.example.credibanco.ui.theme.UI.ItemListDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.credibanco.R
import kotlinx.coroutines.launch


@Composable
fun ItemListDetailView(receiptId: String, statusDescription: String, rrn: String, ItemListDetaiViewModel: ItemListDetaiViewModel, navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_200))

    ) {
        ContentView(navController)
        ContentDescription(receiptId, statusDescription)
        ModalContent(receiptId, rrn, ItemListDetaiViewModel, navController)
    }
}

@Composable
fun ContentView(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
fun ContentDescription(receiptId: String, statusDescription: String) {
    Box (
        modifier = Modifier.padding(20.dp)
    ) {
        Column {
            Text(
                text = receiptId,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Text(
                text = statusDescription,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Text(
                text = "ammount",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalContent(
    receiptId: String,
    rrn: String,
    ItemListDetaiViewModel: ItemListDetaiViewModel,
    navController: NavController
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    var edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Cyan,

                )
    ) {

        ExtendedFloatingActionButton(onClick = { openBottomSheet = !openBottomSheet }, modifier = Modifier.padding(vertical = 50.dp)) {
            Text(text = "Anular Transaction")
            if (openBottomSheet) {
                val windowInsets = if (edgeToEdgeEnabled)
                    WindowInsets(0) else BottomSheetDefaults.windowInsets

                ModalBottomSheet(
                    onDismissRequest = { openBottomSheet = false },
                    sheetState = bottomSheetState,
                    windowInsets = windowInsets
                ) {

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            // Note: If you provide logic outside of onDismissRequest to remove the sheet,
                            // you must additionally handle intended state cleanup, if any.
                            onClick = {
                                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                    if (!bottomSheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                }
                            }
                        ) {
                            Text("Hide Bottom Sheet")
                        }
                    }
                    CancellationScreen(receiptId, rrn, ItemListDetaiViewModel, navController)
                }
            }
        }
    }
}

@Composable
fun CancellationScreen(
    receiptId: String,
    rrn: String,
    ItemListDetaiViewModel: ItemListDetaiViewModel,
    navController: NavController
) {

    val isLoading: Boolean by ItemListDetaiViewModel.isLoading.observeAsState(initial = false)
    val corutineScope = rememberCoroutineScope()



    if(isLoading){
       AlertDialogExample(
            dialogTitle = "Estado de Transaccion",
            dialogText = "Transaccion Aprobada",
            icon = Icons.Filled.CheckCircle
        )
        navController.navigateUp()
    }

    Column(modifier = Modifier.padding(horizontal = 60.dp)) {
        Text(
            text = receiptId,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = rrn,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        AnulmentTransactionButton() {
            corutineScope.launch {
                ItemListDetaiViewModel.onButtonSelected(receiptId, rrn)
            }
        }
    }
}

@Composable
fun AnulmentTransactionButton(onSelected: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onSelected() },
        icon = { },
        text = { Text(text = "Autorizar Transacción") },
        modifier = Modifier.padding(horizontal = 40.dp, vertical = 10.dp)
    )
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

