package com.elian.computeit.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.elian.computeit.core.data.util.CountDownTimerImpl
import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.presentation.util.constants.DATA_STORE_PREFERENCES_NAME
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = DATA_STORE_PREFERENCES_NAME)

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Singleton
    @Provides
    fun provideFirestore() = Firebase.firestore

    @Provides
    fun provideCountDownTimer(): CountDownTimer = CountDownTimerImpl()
}