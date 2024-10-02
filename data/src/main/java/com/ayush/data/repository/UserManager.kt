package com.ayush.data.repository

import com.ayush.data.datastore.UserPreferences
import com.ayush.data.datastore.UserSettings
import com.ayush.data.datastore.toUser
import com.ayush.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val userPreferences: UserPreferences
) {
    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = _userFlow.asStateFlow()

    suspend fun initializeUser() {
        val userData = userPreferences.getUserData()
        _userFlow.value = if (userData.token.isNotEmpty()) userData.toUser() else null
    }

    suspend fun updateUser(user: User?) {
        if (user == null) {
            userPreferences.setUserData(UserSettings())
        } else {
            userPreferences.setUserData(
                UserSettings(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    token = user.token,
                    created = user.created,
                    updated = user.updated
                )
            )
        }
        _userFlow.value = user
    }

    fun isUserLoggedIn(): Boolean = userFlow.value != null
}