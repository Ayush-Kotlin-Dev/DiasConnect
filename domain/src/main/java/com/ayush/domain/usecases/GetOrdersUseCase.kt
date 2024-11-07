package com.ayush.domain.usecases

import com.ayush.domain.model.Result
import com.ayush.domain.model.myOrder
import com.ayush.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(): Result<List<myOrder>> {
        return orderRepository.getOrdersByUserId()
    }
}