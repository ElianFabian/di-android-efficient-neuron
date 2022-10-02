package com.elian.computeit.di

import com.elian.computeit.feature_auth.data.repository.AuthRepositoryImpl
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Singleton
    @Provides
    fun provideTaskRepository(): AuthRepository = AuthRepositoryImpl(Firebase.firestore)
}