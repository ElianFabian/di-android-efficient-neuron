package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.util.*
import com.elian.computeit.data.model.User
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
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

        val newUser = User(
            email = email,
            password = password
        )

        firestore.document("$USERS_COLLECTION/$email").set(newUser).await()

        return Resource.Success()
    }

    private suspend fun getUserByEmail(email: String) = withContext(Dispatchers.IO)
    {
        firestore.document("$USERS_COLLECTION/$email").get().await().toObject(User::class.java)
    }
}