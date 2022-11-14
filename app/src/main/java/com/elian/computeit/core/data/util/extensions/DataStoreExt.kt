package com.elian.computeit.core.data.util.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>) = data.first()[key]

suspend fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, value: T)
{
    edit { it[key] = value }
}