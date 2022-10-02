package com.elian.computeit.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.elian.computeit.core.domain.repository.AppSettingsRepository
import com.elian.computeit.core.util.DataStoreKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppSettingsRepository
{
    override suspend fun getUserEmail(): String?
    {
        return dataStore.data.first()[stringPreferencesKey(DataStoreKeys.userEmail)]
    }

    override suspend fun saveUserEmail(email: String)
    {
        dataStore.edit()
        { 
            it[stringPreferencesKey(DataStoreKeys.userEmail)] = email
        }
    }
}