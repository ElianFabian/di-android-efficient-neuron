package com.elian.computeit.feature_auth.data.repository

import com.elian.computeit.R
import com.elian.computeit.core.data.util.constants.COLLECTION_USERS
import com.elian.computeit.core.domain.models.User
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.UtilRepository
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.params.LoginParams
import com.elian.computeit.feature_auth.domain.params.RegisterParams
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val utilRepository: UtilRepository,
	private val appData: LocalAppDataRepository,
) :
	AuthRepository {

	override suspend fun login(params: LoginParams): SimpleResource = withContext(Dispatchers.IO) {
		val user = utilRepository.getUserByName(params.username) ?: return@withContext Resource.Error(UiText(R.string.error_user_doesnt_exist))

		if (user.password != params.password) return@withContext Resource.Error(UiText(R.string.error_password_is_wrong))

		appData.saveUserUuid(user.uuid)

		Resource.Success()
	}

	override suspend fun register(params: RegisterParams): SimpleResource = withContext(Dispatchers.IO) {
		if (utilRepository.isUsernameTaken(params.username)) return@withContext Resource.Error(UiText(R.string.error_username_is_already_in_use))

		User(
			name = params.username,
			password = params.password,
		).apply {
			appData.saveUserUuid(uuid)

			firestore.document("$COLLECTION_USERS/$uuid")
				.set(this)
				.await()
		}

		return@withContext Resource.Success()
	}
}