package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
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
                appRepository.saveUserUuid(user.uuid)

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
                appRepository.saveUserUuid(uuid)

                Firebase.firestore.document("$COLLECTION_USERS/$uuid").set(this).await()
            }

            Resource.Success()
        }
    }


    private suspend fun getUserByName(name: String): User?
    {
        val users = Firebase.firestore.collection(COLLECTION_USERS)
            .whereEqualTo(User::name.name, name)
            .get()
            .await()

        return if (users.isEmpty) null else users.first().toObject()
    }
}