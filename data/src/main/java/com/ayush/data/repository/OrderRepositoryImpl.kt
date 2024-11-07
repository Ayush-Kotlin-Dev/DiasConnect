package com.ayush.data.repository

import com.apollographql.apollo.ApolloClient
import com.ayush.data.CreateOrderMutation
import com.ayush.data.GetUserOrdersQuery
import com.ayush.data.datastore.UserPreferences
import com.ayush.domain.model.CreateOrderInput
import com.ayush.domain.model.Order
import com.ayush.domain.model.Result
import com.ayush.domain.model.myOrder
import com.ayush.domain.repository.OrderRepository
import javax.inject.Inject
import com.ayush.data.type.OrderItemInput as GraphQLOrderItemInput


class OrderRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dataStore: UserPreferences
) : OrderRepository {
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

    override suspend fun getOrdersByUserId(): Result<List<myOrder>> {
        val userId = dataStore.getUserData().id.toLong()

        return try {
            val response = apolloClient.query(GetUserOrdersQuery(userId)).execute()

            val orders = response.data?.getUserOrders

            if (orders != null) {
                val myOrders = orders.map {
                    myOrder(
                        id = it.id,
                        userId = it.userId,
                        status = com.ayush.domain.model.OrderStatus.valueOf(it.status.name),
                        total = it.total.toFloat(),
                        currency = it.currency,
                        shippingAddress = it.shippingAddress,
                        paymentMethod = it.paymentMethod,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        items = it.items.map {
                            com.ayush.domain.model.OrderItemType(
                                id = it.id,
                                productId = it.productId,
                                quantity = it.quantity,
                                price = it.price
                            )
                        }
                    )
                }
                Result.success(myOrders)
            } else {
                Result.error(Exception("Failed to get orders"))
            }
        } catch (e: Exception) {
            Result.error(e)
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