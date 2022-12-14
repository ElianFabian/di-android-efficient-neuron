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
import dagger.Binds
import dagger.Module
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
	fun bindUtilRepository(impl: UtilRepositoryImpl): UtilRepository

	@Binds
	@Singleton
	fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

	@Binds
	@Singleton
	fun bindLocalAppDataRepository(impl: LocalAppDataRepositoryImpl): LocalAppDataRepository
}

@Module
@InstallIn(ActivityRetainedComponent::class)
interface RepositoryModuleActivityRetainedScoped
{
	@Binds
	@ActivityRetainedScoped
	fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

	@Binds
	@ActivityRetainedScoped
	fun provideTestDataRepository(impl: TestDataRepositoryImpl): TestDataRepository
}