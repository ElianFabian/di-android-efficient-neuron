package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.data.model.User
import com.elian.computeit.core.util.UiText
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
        else Resource.Success(Unit)
    }

    override suspend fun register(email: String, password: String): SimpleResource
    {
        val newUser = User(
            email = email,
            password = password
        )

        if (getUserByEmail(email) != null) return Resource.Error(UiText.StringResource(R.string.error_user_already_exists))

        firestore.document("users/$email").set(newUser).await()

        return Resource.Error()
    }

    private suspend fun getUserByEmail(email: String) = withContext(Dispatchers.IO)
    {
        firestore.document("users/$email").get().await().toObject(User::class.java)
    }
}