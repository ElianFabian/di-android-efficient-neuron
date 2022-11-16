package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.models.UserData
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS_DATA
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TestDataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appRepository: LocalAppDataRepository,
) : TestDataRepository
{
    override suspend fun addTestData(testData: TestData)
    {
        getUserDataRef().update(UserData::testDataList.name, FieldValue.arrayUnion(testData))
    }

    override suspend fun getTestDataList() = flow()
    {
        val userDataFromServer = getUserDataRef().get().await().toObject(UserData::class.java)!!

        emit(userDataFromServer.testDataList)
    }


    private suspend fun getUserDataRef(): DocumentReference
    {
        val userUuid = appRepository.getUserUuid()!!

        return firestore.document("$COLLECTION_USERS_DATA/$userUuid")
    }
}