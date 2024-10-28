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

class OfflineOrderRepository(private val orderDao: OrderDao) : OrderRepository {
    override fun getAllOrderStream(): Flow<List<Order>> = orderDao.getAllOrders()

    override fun getOrderStream(id: Int): Flow<Order?> = orderDao.getOrder(id)

    override suspend fun insertOrder(order: Order) = orderDao.insert(order)

    override suspend fun deleteOrder(order: Order) = orderDao.delete(order)

    override suspend fun updateOrder(order: Order) = orderDao.update(order)
}
