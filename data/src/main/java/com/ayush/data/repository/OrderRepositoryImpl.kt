package com.ayush.data.repository

import com.apollographql.apollo.ApolloClient
import com.ayush.data.CreateOrderMutation
import com.ayush.data.datastore.UserPreferences
import com.ayush.domain.model.CreateOrderInput
import com.ayush.domain.model.Order
import com.ayush.data.type.OrderItemInput as GraphQLOrderItemInput
import com.ayush.domain.repository.OrderRepository
import com.ayush.domain.model.Result
import javax.inject.Inject


class OrderRepositoryImpl  @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dataStore: UserPreferences
): OrderRepository {
    override suspend fun createOrder(
        input: CreateOrderInput
    ): Result<Order> {
        val orderItems = input.items.map {
            GraphQLOrderItemInput(
                productId = it.productId,
                quantity = it.quantity,
                price = it.price
            )
        }
        val userId = dataStore.getUserData().id.toLong()

        val response = apolloClient.mutation(
            CreateOrderMutation(
                userId = userId,
                input = com.ayush.data.type.CreateOrderInput(
                    items = orderItems,
                    paymentMethod = input.paymentMethod,
                    shippingAddress = input.shippingAddress,
                    total = input.total
                )
            )
        ).execute()

        when {
            response.hasErrors() -> {
                return Result.error(Exception(response.errors?.first()?.message))
            }
            else -> {
                val order = response.data?.createOrder?.toOrder()
                if (order != null) {
                    return Result.success(order)
                } else {
                    return Result.error(Exception("Order not found"))
                }
            }
        }
    }

    private fun CreateOrderMutation.CreateOrder.toOrder(): Order {
        return Order(
            id = id,
            userId = userId,
            items = items.map {
                com.ayush.domain.model.OrderItem(
                    id = it.id,
                    productId = it.productId,
                    quantity = it.quantity,
                    price = it.price.toFloat()
                )
            },
            status = com.ayush.domain.model.OrderStatus.valueOf(status.name),
            total = total.toFloat(),
            currency = currency,
            createdAt = createdAt,
            updatedAt = updatedAt

        )
    }
}