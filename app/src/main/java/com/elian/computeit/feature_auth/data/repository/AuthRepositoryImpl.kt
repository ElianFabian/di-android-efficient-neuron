package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appRepository: LocalAppDataRepository,
) :
    AuthRepository
{
    override suspend fun login(email: String, password: String): SimpleResource
    {
        val user = getUserByEmail(email)

        return when
        {
            user == null              -> Resource.Error(R.string.error_user_doesnt_exist)
            user.password != password -> Resource.Error(R.string.error_password_is_wrong)
            else                      ->
            {
                appRepository.apply()
                {
                    saveUserUuid(user.uuid)
                    saveUserEmail(user.email)
                    saveUserName(user.name)
                }

                Resource.Success()
            }
        }
    }

    override suspend fun register(
        email: String,
        name: String,
        password: String,
    ): SimpleResource = when
    {
        getUserByEmail(email) != null -> Resource.Error(R.string.error_email_is_already_in_use)
        getUserByName(name) != null   -> Resource.Error(R.string.error_username_is_already_in_use)
        else                          ->
        {
            User(
                email = email,
                name = name,
                password = password,
            ).apply()
            {
                firestore.document("$COLLECTION_USERS/$uuid").set(this).await()
            }

            Resource.Success()
        }
    }


    private suspend fun getUserByEmail(email: String) = getUserByField(User::email.name, email)

    private suspend fun getUserByName(name: String) = getUserByField(User::name.name, name)

    private suspend fun getUserByField(fieldName: String, value: String) = withContext(Dispatchers.IO)
    {
        val users = firestore.collection(COLLECTION_USERS)
            .whereEqualTo(fieldName, value).get().await()

        if (users.isEmpty) null
        else users.first().toObject(User::class.java)
    }
}