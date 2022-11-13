package com.elian.computeit.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.util.constants.KEY_USERNAME
import com.elian.computeit.core.util.constants.KEY_USER_EMAIL
import com.elian.computeit.core.util.constants.KEY_USER_UUID
import com.elian.computeit.core.util.extensions.get
import com.elian.computeit.core.util.extensions.set
import javax.inject.Inject

class LocalAppDataRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : LocalAppDataRepository
{
    override suspend fun getUserUuid() = dataStore.get(KEY_USER_UUID)
    override suspend fun saveUserUuid(uuid: String) = dataStore.set(KEY_USER_UUID, uuid)

    override suspend fun getUserEmail() = dataStore.get(KEY_USER_EMAIL)
    override suspend fun saveUserEmail(email: String) = dataStore.set(KEY_USER_EMAIL, email)

    override suspend fun getUsername() = dataStore.get(KEY_USERNAME)
    override suspend fun saveUsername(username: String) = dataStore.set(KEY_USERNAME, username)
}