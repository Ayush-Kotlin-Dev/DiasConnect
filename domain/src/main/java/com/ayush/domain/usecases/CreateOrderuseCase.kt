package com.ayush.domain.usecases

import android.util.Log
import com.ayush.domain.model.Order
import com.ayush.domain.model.CreateOrderInput
import com.ayush.domain.repository.OrderRepository
import com.ayush.domain.model.Result
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend operator fun invoke( input: CreateOrderInput): Result<Order> {
        Log.d("CreateOrderUseCase", "Creating order , products: ${input.items}")
        return orderRepository.createOrder( input)
    }
}