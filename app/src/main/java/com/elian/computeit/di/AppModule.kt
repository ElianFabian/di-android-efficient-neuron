package com.elian.computeit.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.elian.computeit.core.data.util.PreciseCountDownTimerImpl
import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.presentation.util.constants.DATA_STORE_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

val Context.dataStore by preferencesDataStore(name = DATA_STORE_PREFERENCES_NAME)

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Provides
    fun provideCountDownTimer(): CountDownTimer = PreciseCountDownTimerImpl()
}