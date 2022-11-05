package com.elian.computeit.feature_tests.data.repository

import com.elian.computeit.core.domain.repository.AppSettingsRepository
import com.elian.computeit.core.util.COLLECTION_USERS_DATA
import com.elian.computeit.core.util.FIELD_TEST_SESSION_DATA_LIST
import com.elian.computeit.feature_tests.data.models.TestSessionData
import com.elian.computeit.feature_tests.domain.repository.TestDataRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TestDataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val settings: AppSettingsRepository,
) : TestDataRepository
{
    override suspend fun addTestSessionData(testSessionData: TestSessionData)
    {
        val userUuid = settings.getCurrentUserUuid()!!
        val userDataRef = firestore.document("$COLLECTION_USERS_DATA/$userUuid")

        val listFromServerSize = userDataRef.get().await().data?.size ?: 0

        if (listFromServerSize == 0)
        {
            userDataRef.set(mapOf(FIELD_TEST_SESSION_DATA_LIST to listOf(testSessionData)))
        }
        else userDataRef.update(FIELD_TEST_SESSION_DATA_LIST, FieldValue.arrayUnion(testSessionData))
    }
}