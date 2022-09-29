package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.data.model.User
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.view_model.SignUpContract
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl :
    AuthRepository
{
    private val firestore get() = Firebase.firestore


    override suspend fun login(email: String, password: String): SimpleResource
    {
        val user = getUserByEmail(email)

        return if (user == null)
        {
            Resource.Error(UiText.DynamicString("Couldn't get user from server"))
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