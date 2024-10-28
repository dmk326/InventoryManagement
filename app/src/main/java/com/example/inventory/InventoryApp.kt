/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.navigation.AppBar
import com.example.inventory.ui.navigation.DrawerBody
import com.example.inventory.ui.navigation.DrawerHeader
import com.example.inventory.ui.navigation.InventoryNavHost
import com.example.inventory.ui.navigation.InventoryScreen
import com.example.inventory.ui.navigation.NavDrawerItem
import com.example.inventory.ui.order.OrdersScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Top level composable that represents screens for the application.
 */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InventoryApp(navController: NavHostController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//    val drawerState = rememberDrawerState(DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            DrawerContent(navController)
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                InventoryTopAppBar(
//                    title = when (currentRoute) {
//                        "inventory" -> "Inventory"
//                        "orders" -> "Orders"
//                        else -> "UMA"
//                    },
//                    canNavigateBack = navController.previousBackStackEntry != null,
//                    navigateUp = { navController.navigateUp() },
//                    openDrawer = { scope.launch {
//                        drawerState.open()
//                    }
//                    }
//                )
//            }
//        ) { innerPadding ->
//            InventoryScreen(
//                navController = navController,
//                modifier = Modifier.padding(innerPadding)
//            )
//        }
//    }
//}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryApp(navController: NavHostController) {
    val items = listOf(
        NavDrawerItem(
            id = "inventory",
            title = "Inventory",
            contentDescription = "Inventory",
            icon = Icons.Filled.Settings
        ),
        NavDrawerItem(
            id = "orders",
            title = "Orders",
            contentDescription = "Orders",
            icon = Icons.Filled.Home
        ))


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable() {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "UMA",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium)
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            println("DMK Clicked on ${item.title}")
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.id) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                        }
                    )
                }
            }
        },
   ) {
        Scaffold (
            topBar = {
                InventoryTopAppBar(
                    scope = scope,
                    drawerState = drawerState,
                    title = when (selectedItemIndex) {
                        0 -> "Inventory"
                        1 -> "Orders"
                        else -> "UMA"
                    }
                )
            }
        ){
            InventoryNavHost(navController)
        }
    }
}


//@Composable
//fun DrawerContent(navController: NavHostController) {
//    Column {
//        Text("Inventory", modifier = Modifier.clickable {
//            navController.navigate("inventory") {
//                popUpTo(navController.graph.startDestinationId) {
//                    saveState = true
//                }
//                launchSingleTop = true
//                restoreState = true
//            }
//        })
//        Text("Orders", modifier = Modifier.clickable {
//            navController.navigate("orders") {
//                popUpTo(navController.graph.startDestinationId) {
//                    saveState = true
//                }
//                launchSingleTop = true
//                restoreState = true
//            }
//        })
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopAppBar(
    title: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
//    CenterAlignedTopAppBar(
//        title = { Text(title) },
//        modifier = modifier,
//        scrollBehavior = scrollBehavior,
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = stringResource(R.string.back_button)
//                    )
//                }
//            } else {
//                IconButton(onClick = openDrawer) {
//                    Icon(
//                        imageVector = Icons.Filled.Menu,
//                        contentDescription = stringResource(R.string.menu_button)
//                    )
//                }
//            }
//        }
//    )
}

//@Preview(showBackground = true)
//@Composable
//fun InventoryAppPreview() {
//    InventoryApp(rememberNavController())
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DrawerContentPreview() {
//    val navController = rememberNavController()
//    DrawerContent(navController)
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun InventoryTopAppBarPreview() {
//    InventoryTopAppBar(
//        title = "UMA",
//        canNavigateBack = false,
//        openDrawer = {}
//    )
//}