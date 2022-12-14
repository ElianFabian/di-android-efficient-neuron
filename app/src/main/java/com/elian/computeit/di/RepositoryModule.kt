package com.elian.computeit.di

import com.elian.computeit.core.data.repository.LocalAppDataRepositoryImpl
import com.elian.computeit.core.data.repository.TestDataRepositoryImpl
import com.elian.computeit.core.data.repository.UtilRepositoryImpl
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.elian.computeit.core.domain.repository.UtilRepository
import com.elian.computeit.feature_auth.data.repository.AuthRepositoryImpl
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.feature_profile.data.repository.ProfileRepositoryImpl
import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule
{
	@Binds
	@Singleton
	fun bindUtilRepository(repository: UtilRepositoryImpl): UtilRepository

	@Binds
	@Singleton
	fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

	@Binds
	@Singleton
	fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository

	@Binds
	@Singleton
	fun bindLocalAppDataRepository(repository: LocalAppDataRepositoryImpl): LocalAppDataRepository
}

// I would like to limit the scope of this Repository with a @Binds but I haven't found any way to do it
@Module
@InstallIn(ActivityRetainedComponent::class)
class RepositoryModuleActivityRetainedScoped
{
	@Provides
	@ActivityRetainedScoped
	fun provideTestDataRepository(
		firestore: FirebaseFirestore,
		appData: LocalAppDataRepository,
	): TestDataRepository
	{
		return TestDataRepositoryImpl(
			firestore = firestore,
			appData = appData,
		)
	}
}