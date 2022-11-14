package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.util.constants.COLLECTION_USERS_DATA
import com.elian.computeit.core.data.util.constants.FIELD_TEST_SESSION_DATA_LIST
import com.elian.computeit.core.domain.models.TestSessionData
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
    override suspend fun addTestSessionData(testSessionData: TestSessionData)
    {
        getUserDataRef().update(FIELD_TEST_SESSION_DATA_LIST, FieldValue.arrayUnion(testSessionData))
    }

    override suspend fun getTestSessionDataList() = flow()
    {
        val userData = getUserDataRef().get().await().data

        @Suppress("UNCHECKED_CAST")
        val listFromServer = userData?.get(FIELD_TEST_SESSION_DATA_LIST) as? List<TestSessionData> ?: emptyList()

        emit(listFromServer)
    }


    private suspend fun getUserDataRef(): DocumentReference
    {
        val userUuid = appRepository.getUserUuid()!!

        return firestore.document("$COLLECTION_USERS_DATA/$userUuid")
    }
}