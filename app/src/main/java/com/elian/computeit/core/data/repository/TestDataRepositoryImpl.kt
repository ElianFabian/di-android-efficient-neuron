package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.mapper.toTestCountPerSpeedRange
import com.elian.computeit.core.data.model.TestData
import com.elian.computeit.core.data.model.UserData
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS_DATA
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
) : TestDataRepository
{
	private val _listOfTestData = mutableListOf<TestData>()


	override val isDataCached get() = _isDataCached
	private var _isDataCached = false


	override suspend fun addTestData(
		userUuid: String,
		testData: TestData,
	): Unit = withContext(Dispatchers.IO)
	{
		_listOfTestData.add(testData)

		getUserDataRef(userUuid).update(UserData::listOfTestData.name, FieldValue.arrayUnion(testData)).await()
	}

	override suspend fun getListOfTestData(userUuid: String): List<TestData> = withContext(Dispatchers.IO)
	{
		return@withContext if (_listOfTestData.isEmpty())
		{
			val listFromServer = getUserDataRef(userUuid)
				.get()
				.await()
				.toObject<UserData>()!!.listOfTestData!!

			_listOfTestData.addAll(listFromServer)

			_isDataCached = true

			listFromServer
		}
		else _listOfTestData
	}

	override fun getTestCountPerSpeedRange(rangeLength: Int): List<Int> = _listOfTestData.toTestCountPerSpeedRange(rangeLength)


	private suspend fun getUserDataRef(userUuid: String) = withContext(Dispatchers.IO)
	{
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