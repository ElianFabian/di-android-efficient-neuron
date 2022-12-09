package com.elian.computeit.feature_profile.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.data.util.constants.FOLDER_USERS_PROFILE_PICS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.UtilRepository
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.core.util.constants.defaultDateFormat
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val storage: FirebaseStorage,
	private val utilRepository: UtilRepository,
	private val appRepository: LocalAppDataRepository,
) : ProfileRepository
{
	override suspend fun getProfileInfo() = withContext(Dispatchers.IO)
	{
		val userUuid = appRepository.getUserUuid()

		val user = firestore.document("$COLLECTION_USERS/$userUuid")
			.get()
			.await()
			.toObject<User>()!!

		val maxDownloadSize = 5L * 1024 * 1024

		val profilePicBytes = user.profilePicUuid?.let()
		{
			storage.reference
				.child("$FOLDER_USERS_PROFILE_PICS/${user.profilePicUuid}")
				.getBytes(maxDownloadSize)
				.await()
		}

		return@withContext user.run()
		{
			ProfileInfo(
				username = name,
				biography = biography,
				profilePicBytes = profilePicBytes?.toList() ?: emptyList(),
				createdAt = defaultDateFormat.format(Date(createdAtUnix)),
			)
		}
	}

	override suspend fun updateProfileInfo(
		username: String,
		biography: String,
		profilePicBytes: List<Byte>,
	): SimpleResource = withContext(Dispatchers.IO)
	{
		val userUuid = appRepository.getUserUuid()!!
		val currentUser = utilRepository.getUserByUuid(userUuid)!!

		utilRepository.getUserByName(username).let()
		{
			val isUsernameInUse = (it != null) && (it.name != currentUser.name) && (it.name == username)

			if (isUsernameInUse) return@withContext Resource.Error(R.string.error_username_is_already_in_use)
		}

		var profilePicUuid: String? = currentUser.profilePicUuid

		try
		{
			if (profilePicBytes.isNotEmpty())
			{
				profilePicUuid = profilePicUuid ?: UUID.randomUUID().toString()

				storage.reference.child("$FOLDER_USERS_PROFILE_PICS/$profilePicUuid").putBytes(profilePicBytes.toByteArray()).await()
			}
		}
		catch (e: Exception)
		{
			return@withContext Resource.Error(e.message)
		}

		firestore.document("$COLLECTION_USERS/$userUuid")
			.update(
				User::name.name, username,
				User::biography.name, biography,
				User::profilePicUuid.name, profilePicUuid,
			).await()

		return@withContext Resource.Success()
	}

	override suspend fun logout()
	{
		appRepository.saveUserUuid("")
	}
}