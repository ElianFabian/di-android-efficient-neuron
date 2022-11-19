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
    override suspend fun login(username: String, password: String): SimpleResource
    {
        val user = getUserByName(username)

        return when
        {
            user == null              -> Resource.Error(R.string.error_user_doesnt_exist)
            user.password != password -> Resource.Error(R.string.error_password_is_wrong)
            else                      ->
            {
                appRepository.apply()
                {
                    saveUserUuid(user.uuid)
                    saveUserName(user.name)
                }

                Resource.Success()
            }
        }
    }

    override suspend fun register(
        username: String,
        password: String,
    ): SimpleResource = when
    {
        getUserByName(username) != null -> Resource.Error(R.string.error_username_is_already_in_use)
        else                            ->
        {
            User(
                name = username,
                password = password,
            ).apply()
            {
                firestore.document("$COLLECTION_USERS/$uuid").set(this).await()
            }

            Resource.Success()
        }
    }


    private suspend fun getUserByName(name: String) = getUserByField(User::name.name, name)

    private suspend fun getUserByField(fieldName: String, value: String) = withContext(Dispatchers.IO)
    {
        val users = firestore.collection(COLLECTION_USERS)
            .whereEqualTo(fieldName, value).get().await()

        if (users.isEmpty) null
        else users.first().toObject(User::class.java)
    }
}