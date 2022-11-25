package com.elian.computeit.di

import com.elian.computeit.core.data.repository.LocalAppDataRepositoryImpl
import com.elian.computeit.core.data.repository.TestDataRepositoryImpl
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.elian.computeit.feature_auth.data.repository.AuthRepositoryImpl
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.feature_profile.data.repository.ProfileRepositoryImpl
import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule
{
    @Binds
    @Singleton
    fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindTestDataRepository(repository: TestDataRepositoryImpl): TestDataRepository

    @Binds
    @Singleton
    fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun bindLocalAppDataRepository(repository: LocalAppDataRepositoryImpl): LocalAppDataRepository
}