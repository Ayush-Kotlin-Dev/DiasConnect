package com.ayush.domain.repository

interface ProfileRepository  {

    suspend fun getProfileData() : String
}