package com.elian.computeit.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.elian.computeit.core.data.util.constants.KEY_USER_UUID
import com.elian.computeit.core.data.util.extensions.get
import com.elian.computeit.core.data.util.extensions.set
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import javax.inject.Inject

class LocalAppDataRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : LocalAppDataRepository
{
    override suspend fun getUserUuid() = dataStore.get(KEY_USER_UUID)
    override suspend fun saveUserUuid(uuid: String) = dataStore.set(KEY_USER_UUID, uuid)
}