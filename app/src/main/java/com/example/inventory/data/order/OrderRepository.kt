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

package com.example.inventory.data.order

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Order] from a given data source.
 */
interface OrderRepository {
    /**
     * Retrieve all the orders from the the given data source.
     */
    fun getAllOrderStream(): Flow<List<Order>>

    /**
     * Retrieve an order from the given data source that matches with the [id].
     */
    fun getOrderStream(id: Int): Flow<Order?>

    /**
     * Insert order in the data source
     */
    suspend fun insertOrder(order: Order)

    /**
     * Delete order from the data source
     */
    suspend fun deleteOrder(order: Order)

    /**
     * Update order in the data source
     */
    suspend fun updateOrder(order: Order)
}
