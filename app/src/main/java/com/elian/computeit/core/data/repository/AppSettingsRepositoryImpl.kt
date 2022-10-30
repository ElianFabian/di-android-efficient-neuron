package com.elian.computeit.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.elian.computeit.core.domain.repository.AppSettingsRepository
import com.elian.computeit.core.util.KEY_USER_EMAIL
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppSettingsRepository
{
    override suspend fun getCurrentUserUuid(): String?
    {
        return dataStore.data.first()[KEY_USER_EMAIL]
    }

    override suspend fun saveCurrentUserUuid(uuid: String)
    {
        dataStore.edit { it[KEY_USER_EMAIL] = uuid }
    }
}