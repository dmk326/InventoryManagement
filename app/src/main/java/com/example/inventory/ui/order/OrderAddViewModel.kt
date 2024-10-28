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

package com.example.inventory.ui.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.order.Order
import com.example.inventory.data.order.OrderRepository
import com.example.inventory.data.order.OrderUnit
import java.text.NumberFormat
import java.util.Locale

/**
 * ViewModel to validate and insert orders in the Room database.
 */
class OrderAddViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    /**
     * Holds current order ui state
     */
    var orderUiState by mutableStateOf(OrderUiState())
        private set

    /**
     * Updates the [orderUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(orderDetails: OrderDetails) {
        orderUiState =
            OrderUiState(orderDetails = orderDetails, isEntryValid = validateInput(orderDetails))
    }

    /**
     * Inserts an [Order] in the Room database
     */
    suspend fun saveOrder() {
        if (validateInput()) {
            orderRepository.insertOrder(orderUiState.orderDetails.toOrder())
        }
    }

    private fun validateInput(uiState: OrderDetails = orderUiState.orderDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Order.
 */
data class OrderUiState(
    val orderDetails: OrderDetails = OrderDetails(),
    val isEntryValid: Boolean = false
)

data class OrderDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val unit: OrderUnit = OrderUnit.KG
)

/**
 * Extension function to convert [OrderUiState] to [Order]. If the value of [OrderDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [OrderUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun OrderDetails.toOrder(): Order = Order(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0,
    unit = unit,
)

fun Order.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(price)
}

/**
 * Extension function to convert [Order] to [OrderUiState]
 */
fun Order.toOrderUIState(isEntryValid: Boolean = false): OrderUiState = OrderUiState(
    orderDetails = this.toOrderDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Order] to [OrderDetails]
 */
fun Order.toOrderDetails(): OrderDetails = OrderDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    unit = unit
)
