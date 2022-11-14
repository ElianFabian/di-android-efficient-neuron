package com.elian.computeit.core.data.repository

import com.elian.computeit.core.data.util.constants.COLLECTION_USERS_DATA
import com.elian.computeit.core.data.util.constants.FIELD_TEST_SESSION_DATA_LIST
import com.elian.computeit.core.domain.models.TestSessionData
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.TestDataRepository
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
        val userUuid = appRepository.getUserUuid()!!
        val userDataRef = firestore.document("$COLLECTION_USERS_DATA/$userUuid")
        val userData = userDataRef.get().await()

        val listFromServer = userData.data?.get(FIELD_TEST_SESSION_DATA_LIST) as List<*>

        if (listFromServer.isEmpty())
        {
            userDataRef.set(mapOf(FIELD_TEST_SESSION_DATA_LIST to listOf(testSessionData)))
        }
        else userDataRef.update(FIELD_TEST_SESSION_DATA_LIST, FieldValue.arrayUnion(testSessionData))
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun getTestSessionDataList() = flow()
    {
        val userUuid = appRepository.getUserUuid()!!
        val userDataRef = firestore.document("$COLLECTION_USERS_DATA/$userUuid")
        val userData = userDataRef.get().await()

        val listFromServer = userData.data?.get(FIELD_TEST_SESSION_DATA_LIST) as List<TestSessionData>

        emit(listFromServer)
    }
}