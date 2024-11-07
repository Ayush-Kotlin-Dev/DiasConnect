package com.ayush.domain.repository


import com.ayush.domain.model.Order
import com.ayush.domain.model.CreateOrderInput
import com.ayush.domain.model.Result
import com.ayush.domain.model.myOrder

interface OrderRepository {
    suspend fun createOrder( input: CreateOrderInput): Result<Order>
    suspend fun getOrdersByUserId(): Result<List<myOrder>>

}