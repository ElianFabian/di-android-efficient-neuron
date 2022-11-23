package com.elian.computeit.feature_profile.data.repository

import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.util.constants.dayMonthYearFormat
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appRepository: LocalAppDataRepository,
) : ProfileRepository
{
    override suspend fun getProfileInfo() = flow()
    {
        val userUuid = appRepository.getUserUuid()

        val user = firestore.document("$COLLECTION_USERS/$userUuid")
            .get()
            .await()
            .toObject<User>()!!

        val profileInfo = user.run()
        {
            ProfileInfo(
                username = name,
                profilePicUrl = profilePicUrl,
                createdAt = dayMonthYearFormat.format(Date(createdAtInSeconds * 1000L)),
            )
        }

        emit(profileInfo)
    }
}