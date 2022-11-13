package com.elian.computeit.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.elian.computeit.core.data.repository.LocalAppDataRepositoryImpl
import com.elian.computeit.core.data.util.PreciseCountDownTimerImpl
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.domain.util.DATA_STORE_PREFERENCES_NAME
import com.elian.computeit.feature_auth.data.repository.AuthRepositoryImpl
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.feature_tests.data.repository.TestDataRepositoryImpl
import com.elian.computeit.feature_tests.domain.repository.TestDataRepository
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
    fun provideAuthRepository(appRepository: LocalAppDataRepository): AuthRepository = AuthRepositoryImpl(
        firestore = Firebase.firestore,
        appRepository = appRepository
    )

    @Singleton
    @Provides
    fun provideTestDataRepository(appDataRepository: LocalAppDataRepository): TestDataRepository = TestDataRepositoryImpl(
        firestore = Firebase.firestore,
        appRepository = appDataRepository
    )

    @Singleton
    @Provides
    fun provideLocalAppDataRepository(@ApplicationContext context: Context): LocalAppDataRepository
    {
        return LocalAppDataRepositoryImpl(context.dataStore)
    }

    @Provides
    fun provideCountDownTimer(): CountDownTimer = PreciseCountDownTimerImpl()
}