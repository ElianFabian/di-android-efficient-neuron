package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.domain.repository.AppSettingsRepository
import com.elian.computeit.core.util.COLLECTION_USERS
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.data.model.User
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val settings: AppSettingsRepository,
) :
    AuthRepository
{
    override suspend fun login(email: String, password: String): SimpleResource
    {
        val user = getUserByEmail(email)

        return if (user == null)
        {
            Resource.Error(UiText.StringResource(R.string.error_user_doesnt_exist))
        }
        else if (user.password != password)
        {
            Resource.Error(UiText.StringResource(R.string.error_password_is_wrong))
        }
        else Resource.Success()
    }

    override suspend fun register(email: String, password: String): SimpleResource
    {
        if (getUserByEmail(email) != null) return Resource.Error(UiText.StringResource(R.string.error_user_already_exists))

        User(
            email = email,
            password = password
        ).apply()
        {
            settings.saveCurrentUserUuid(uuid)

            firestore.document("$COLLECTION_USERS/$uuid").set(this).await()
        }

        return Resource.Success()
    }

    private suspend fun getUserByEmail(email: String) = withContext(Dispatchers.IO)
    {
        val users = firestore.collection(COLLECTION_USERS)
            .whereEqualTo(User::email.name, email).get().await()

        if (users.isEmpty) null
        else users.first().toObject(User::class.java)
    }
}