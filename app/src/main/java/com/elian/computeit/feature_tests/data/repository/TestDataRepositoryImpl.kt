package com.elian.computeit.feature_tests.data.repository

import com.elian.computeit.core.domain.repository.AppSettingsRepository
import com.elian.computeit.core.util.COLLECTION_USERS_DATA
import com.elian.computeit.core.util.FIELD_TEST_SESSION_DATA_LIST
import com.elian.computeit.feature_tests.data.models.TestSessionData
import com.elian.computeit.feature_tests.domain.repository.TestDataRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TestDataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val settings: AppSettingsRepository,
) : TestDataRepository
{
    override suspend fun saveTestSessionData(testSessionData: TestSessionData)
    {
        val userUuid = settings.getCurrentUserUuid()!!

        firestore.document("$COLLECTION_USERS_DATA/$userUuid")
            .update(FIELD_TEST_SESSION_DATA_LIST, FieldValue.arrayUnion(testSessionData))
    }
}