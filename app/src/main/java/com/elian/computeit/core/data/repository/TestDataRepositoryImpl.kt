package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.model.UserData
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS_DATA
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.elian.computeit.feature_tests.domain.model.toTestListInfo
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
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

    override fun getTestListInfo() = flow()
    {
        val listFromServer = getTestDataList()

        emit(listFromServer.toTestListInfo())
    }


    private suspend fun getTestDataList(): List<TestData>
    {
        return getUserDataRef()
            .get()
            .await()
            .toObject<UserData>()!!.testDataList!!
    }


    private suspend fun getUserDataRef(): DocumentReference
    {
        val userUuid = appRepository.getUserUuid()!!

        val documentRef = firestore.document("$COLLECTION_USERS_DATA/$userUuid")
        val snapShot = documentRef.get().await()
        val userData = snapShot.toObject<UserData>()

        return documentRef.apply()
        {
            if (!snapShot.exists() || userData?.testDataList == null)
            {
                set(UserData(emptyList()), SetOptions.merge())
            }
        }
    }
}