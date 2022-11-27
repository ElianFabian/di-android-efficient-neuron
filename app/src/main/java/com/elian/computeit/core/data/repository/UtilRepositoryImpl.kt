package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.UtilRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UtilRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UtilRepository
{
    override suspend fun getUserByUuid(uuid: String): User?
    {
        return firestore.document("$COLLECTION_USERS/$uuid").get().await().toObject()
    }

    override suspend fun getUserByName(name: String): User?
    {
        val users = firestore.collection(COLLECTION_USERS)
            .whereEqualTo(User::name.name, name)
            .get()
            .await()

        return if (users.isEmpty) null else users.first().toObject()
    }
}