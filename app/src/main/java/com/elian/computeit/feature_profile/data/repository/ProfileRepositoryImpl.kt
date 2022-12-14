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
import com.elian.computeit.feature_profile.domain.params.UpdateProfileParams
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
	private val appData: LocalAppDataRepository,
) : ProfileRepository
{
	override suspend fun getProfileInfo() = withContext(Dispatchers.IO)
	{
		val userUuid = appData.getUserUuid()

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

	// TODO-low: avoid uploading profile pic or info if it wasn't updated
	override suspend fun updateProfileInfo(params: UpdateProfileParams): SimpleResource = withContext(Dispatchers.IO)
	{
		val userUuid = appData.getUserUuid()!!
		val currentUser = utilRepository.getUserByUuid(userUuid)!!

		if (utilRepository.isUsernameTaken(
				currentName = currentUser.name,
				newName = params.username,
			)
		) return@withContext Resource.Error(R.string.error_username_is_already_in_use)

		var profilePicUuid: String? = currentUser.profilePicUuid

		try
		{
			if (params.profilePicBytes.isNotEmpty())
			{
				profilePicUuid = profilePicUuid ?: UUID.randomUUID().toString()

				storage.reference.child("$FOLDER_USERS_PROFILE_PICS/$profilePicUuid")
					.putBytes(params.profilePicBytes.toByteArray())
					.await()
			}
			else if (profilePicUuid != null)
			{
				storage.reference.child("$FOLDER_USERS_PROFILE_PICS/$profilePicUuid").delete().await()
				profilePicUuid = null
			}
		}
		catch (e: Exception)
		{
			return@withContext Resource.Error(e.message)
		}

		firestore.document("$COLLECTION_USERS/$userUuid")
			.update(
				User::name.name, params.username,
				User::biography.name, params.biography,
				User::profilePicUuid.name, profilePicUuid,
			).await()

		return@withContext Resource.Success()
	}

	override suspend fun logout()
	{
		appData.saveUserUuid("")
	}
}