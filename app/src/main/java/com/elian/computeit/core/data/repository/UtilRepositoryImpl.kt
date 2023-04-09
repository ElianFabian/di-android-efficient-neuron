package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.UtilRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UtilRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
) : UtilRepository
{
	override suspend fun getUserByUuid(uuid: String): User? = withContext(Dispatchers.IO)
	{
		firestore.document("$COLLECTION_USERS/$uuid").get().await().toObject()
	}

	override suspend fun getUserByName(name: String): User? = withContext(Dispatchers.IO)
	{
		val users = firestore.collection(COLLECTION_USERS)
			.whereEqualTo(User::name.name, name)
			.get()
			.await()

		if (users.isEmpty) null else users.first().toObject()
	}

	override suspend fun isUsernameTaken(currentName: String, newName: String): Boolean = withContext(Dispatchers.IO)
	{
		getUserByName(newName).let()
		{
			(it != null)
				&& !currentName.equals(it.name, ignoreCase = true)
				&& newName.equals(it.name, ignoreCase = true)
		}
	}

	override suspend fun isUsernameTaken(name: String): Boolean = withContext(Dispatchers.IO)
	{
		getUserByName(name) != null
	}
}