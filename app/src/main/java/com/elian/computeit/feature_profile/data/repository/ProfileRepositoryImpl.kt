package com.elian.computeit.feature_profile.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.UtilRepository
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
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
	private val utilRepository: UtilRepository,
	private val appRepository: LocalAppDataRepository,
) : ProfileRepository
{
	override fun getProfileInfo() = flow()
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
				biography = biography,
				profilePicUrl = profilePicUrl,
				createdAt = dayMonthYearFormat.format(Date(createdAtUnix)),
			)
		}

		emit(profileInfo)
	}

	override suspend fun updateProfileInfo(
		username: String,
		biography: String,
		//profilePicUrl: String,
	): SimpleResource
	{
		val userUuid = appRepository.getUserUuid()!!

		val currentUsername = utilRepository.getUserByUuid(userUuid)!!.name

		utilRepository.getUserByName(username).let()
		{
			val isUsernameTaken = it != null && it.name != currentUsername && it.name == username

			if (isUsernameTaken) return Resource.Error(R.string.error_username_is_already_in_use)
		}

		firestore.document("$COLLECTION_USERS/$userUuid")
			.update(
				User::name.name, username,
				User::biography.name, biography,
			).await()

		return Resource.Success()
	}

	override suspend fun logout()
	{
		appRepository.saveUserUuid("")
	}
}