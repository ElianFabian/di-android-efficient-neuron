package com.elian.computeit.core.presentation.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest

fun <T> LifecycleOwner.collectFlowWhenStarted(state: Flow<T>, collector: FlowCollector<T>)
{
	lifecycleScope.launchWhenStarted()
	{
		state.collect(collector)
	}
}

fun <T> LifecycleOwner.collectLatestFlowWhenStarted(state: Flow<T>, action: suspend (value: T) -> Unit)
{
	lifecycleScope.launchWhenStarted()
	{
		state.collectLatest(action)
	}
}