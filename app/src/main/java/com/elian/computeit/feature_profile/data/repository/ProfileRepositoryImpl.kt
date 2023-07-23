package com.elian.computeit.feature_profile.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.data.util.constants.FOLDER_USERS_PROFILE_PICS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.UtilRepository
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.core.util.UiText
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
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val storage: FirebaseStorage,
	private val utilRepository: UtilRepository,
	private val appData: LocalAppDataRepository,
) : ProfileRepository {

	override suspend fun getProfileInfo(userUuid: String) = withContext(Dispatchers.IO) {
		val user = firestore.document("$COLLECTION_USERS/$userUuid")
			.get()
			.await()
			.toObject<User>()!!

		val maxDownloadSizeBytes = 5L * 1024 * 1024

		val profilePicBytes = user.profilePicUuid?.let {
			storage.reference
				.child("$FOLDER_USERS_PROFILE_PICS/${user.profilePicUuid}")
				.getBytes(maxDownloadSizeBytes)
				.await()
		}

		return@withContext user.run {
			ProfileInfo(
				username = name,
				biography = biography,
				profilePicBytes = profilePicBytes?.toList().orEmpty(),
				createdAt = defaultDateFormat.format(Date(createdAtUnix)),
			)
		}
	}

	override suspend fun updateProfileInfo(params: UpdateProfileParams): SimpleResource = withContext(Dispatchers.IO) {
		val currentUser = utilRepository.getUserByUuid(params.userUuid)!!

		val isUsernameTaken = utilRepository.isUsernameTaken(
			currentName = currentUser.name,
			newName = params.username,
		)

		if (isUsernameTaken) return@withContext Resource.Error(UiText(R.string.Error_UsernameIsAlreadyInUse))

		val newOrCurrentProfilePicUuid = currentUser.profilePicUuid ?: UUID.randomUUID().toString()
		try {
			val shouldUpdateOrCreateProfilePic = params.profilePicBytes.isNotEmpty()
			if (shouldUpdateOrCreateProfilePic) {
				storage.reference.child("$FOLDER_USERS_PROFILE_PICS/$newOrCurrentProfilePicUuid")
					.putBytes(params.profilePicBytes.toByteArray())
					.await()
			}
			else if (currentUser.profilePicUuid != null) {
				storage.reference.child("$FOLDER_USERS_PROFILE_PICS/${currentUser.profilePicUuid}")
					.delete()
					.await()
			}
		}
		catch (e: Exception) {
			return@withContext Resource.Error(UiText(e.message))
		}

		firestore.document("$COLLECTION_USERS/${params.userUuid}")
			.update(
				User::name.name, params.username,
				User::biography.name, params.biography,
				User::profilePicUuid.name, newOrCurrentProfilePicUuid,
			).await()

		return@withContext Resource.Success()
	}

	override suspend fun logout() {
		appData.saveUserUuid("")
	}
}