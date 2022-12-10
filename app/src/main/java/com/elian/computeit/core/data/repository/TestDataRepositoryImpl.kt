package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.model.UserData
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS_DATA
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TestDataRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val appRepository: LocalAppDataRepository,
) : TestDataRepository
{
	private val _listOfTestData = mutableListOf<TestData>()


	override suspend fun addTestData(testData: TestData): Unit = withContext(Dispatchers.IO)
	{
		_listOfTestData.add(testData)

		getUserDataRef().update(UserData::listOfTestData.name, FieldValue.arrayUnion(testData))
	}

	override suspend fun getListOfTestData(): List<TestData> = withContext(Dispatchers.IO)
	{
		return@withContext if (_listOfTestData.isEmpty())
		{
			val listFromServer = getUserDataRef()
				.get()
				.await()
				.toObject<UserData>()!!.listOfTestData!!

			_listOfTestData.addAll(listFromServer)

			listFromServer
		}
		else _listOfTestData
	}


	private suspend fun getUserDataRef() = withContext(Dispatchers.IO)
	{
		val userUuid = appRepository.getUserUuid()!!

		val documentRef = firestore.document("$COLLECTION_USERS_DATA/$userUuid")
		val snapShot = documentRef.get().await()
		val userData = snapShot.toObject<UserData>()

		documentRef.apply()
		{
			if (!snapShot.exists() || userData?.listOfTestData == null)
			{
				set(UserData(emptyList()), SetOptions.merge())
			}
		}
	}
}