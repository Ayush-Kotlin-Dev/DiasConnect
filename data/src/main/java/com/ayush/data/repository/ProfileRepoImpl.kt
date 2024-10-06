package com.ayush.data.repository

import androidx.datastore.core.DataStore
import com.ayush.data.datastore.UserSettings
import com.ayush.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileRepoImpl @Inject constructor (
    private val dataStore: DataStore<UserSettings>
) : ProfileRepository {

    override suspend fun getProfileData() = dataStore.data.first().cartId
}