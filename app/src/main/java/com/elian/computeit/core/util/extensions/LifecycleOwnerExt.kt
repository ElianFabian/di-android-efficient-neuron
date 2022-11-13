package com.elian.computeit.core.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

fun <T> LifecycleOwner.collectFlowWhenStarted(state: Flow<T>, action: suspend (value: T) -> Unit)
{
    lifecycleScope.launchWhenStarted()
    {
        state.collect(action)
    }
}

fun <T> LifecycleOwner.collectLatestFlowWhenStarted(state: Flow<T>, action: suspend (value: T) -> Unit)
{
    lifecycleScope.launchWhenStarted()
    {
        state.collectLatest(action)
    }
}