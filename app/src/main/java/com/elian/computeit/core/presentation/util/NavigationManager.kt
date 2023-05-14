package com.elian.computeit.core.presentation.util

import androidx.navigation.NavDestination

class NavigationManager(
	private val onDestinationChangedEvents: Collection<NavigationEvent> = emptyList(),
	private val onBackgroundedEvents: Collection<NavigationEvent> = emptyList(),
)
{
	private var _currentDestination: NavDestination? = null
	private var _previousDestination: NavDestination? = null

	fun onDestinationChanged(destination: NavDestination)
	{
		_currentDestination = destination

		triggerEvents(onDestinationChangedEvents)

		_previousDestination = destination
	}

	fun onBackgrounded() = triggerEvents(onBackgroundedEvents)


	private fun triggerEvents(events: Collection<NavigationEvent>)
	{
		for (navigationEvent in events) when (navigationEvent)
		{
			is DestinationEvent ->
			{
				if (_currentDestination?.id in navigationEvent.destinations)
				{
					navigationEvent.onEvent()
				}
				else navigationEvent.onOtherEvent?.invoke()
			}

			is ActionEvent      ->
			{
				if (navigationEvent.actions.any { it.first == _previousDestination?.id && it.second == _currentDestination?.id })
				{
					navigationEvent.onEvent()
				}
				else navigationEvent.onOtherEvent?.invoke()
			}
		}
	}
}

sealed class NavigationEvent(
	val onEvent: () -> Unit,
	val onOtherEvent: (() -> Unit)? = null,
)


class DestinationEvent(
	val destinations: Collection<Int>,
	onEvent: () -> Unit,
	onOtherEvent: (() -> Unit)? = null,
) : NavigationEvent(onEvent, onOtherEvent)

class ActionEvent(
	val actions: Collection<Pair<Int, Int>>,
	onEvent: () -> Unit,
	onOtherEvent: (() -> Unit)? = null,
) : NavigationEvent(onEvent, onOtherEvent)