package com.example.credibanco

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.credibanco.ui.theme.UI.TransactionScreen.AuthorizationScreen
import com.example.credibanco.ui.theme.UI.Home.Home
import com.example.credibanco.ui.theme.UI.Home.HomeViewModel
import com.example.credibanco.ui.theme.UI.ItemListDetail.ItemListDetaiViewModel
import com.example.credibanco.ui.theme.UI.ItemListDetail.ItemListDetailView
import com.example.credibanco.ui.theme.UI.ListTransactionsView.ListTransactionsView
import com.example.credibanco.ui.theme.UI.ListTransactionsView.ListTransactionsViewModel
import com.example.credibanco.ui.theme.UI.TransactionScreen.TransactionViewModel
import com.example.credibanco.ui.theme.UI.Theme.CredibanCoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context: Context = this

            NavHost(
                navController = navController,
                startDestination = "Home"
            ) {
                composable(route = "Home") {
                    CredibanCoTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Home(
                                HomeViewModel(context),
                                navController,
                            )
                        }
                    }
                }
                composable(route = "AuthorizationScreen") {
                    AuthorizationScreen(
                        TransactionViewModel(context),
                        navController,
                        onClickToNavigate = {
                            navController.navigateUp()
                        }

                    )
                }
                composable(route = "ListTransactionsView") {
                    ListTransactionsView(
                        ListTransactionsViewModel(context),
                        navController,
                        navigateToView = {
                            navController.navigate("")
                        }
                    )
                }
                composable(route = "ItemListDetailView/{receiptId}/{statusDescription}/{rrn}",
                    arguments = listOf(
                        navArgument("receiptId") {
                            type = NavType.StringType
                        },
                        navArgument("statusDescription") {
                            type = NavType.StringType
                        },
                        navArgument("rrn") {
                            type = NavType.StringType
                        }
                    )) { backStackEntry ->
                    val receiptId = backStackEntry.arguments?.getString("receiptId")
                    val statusDescription = backStackEntry.arguments?.getString("statusDescription")
                    val rrn = backStackEntry.arguments?.getString("rrn")

                    receiptId?.let {
                        statusDescription?.let { it1 ->
                            rrn?.let {it2 ->
                                ItemListDetailView(
                                    receiptId = it,
                                    statusDescription = it1,
                                    rrn = it2,
                                    ItemListDetaiViewModel(context)
                                )

                            }
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CredibanCoTheme {

    }
}