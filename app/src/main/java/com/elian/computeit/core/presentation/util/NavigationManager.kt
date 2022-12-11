package com.elian.computeit.core.presentation.util

import androidx.navigation.NavDestination

class NavigationManager(
	private val onDestinationChangedBehaviours: Collection<NavigationBehaviour> = emptyList(),
	private val onBackgroundedBehaviours: Collection<NavigationBehaviour> = emptyList(),
)
{
	private var _currentDestination: NavDestination? = null
	private var _previousDestination: NavDestination? = null

	fun onDestinationChanged(destination: NavDestination)
	{
		_currentDestination = destination

		executeBehaviours(onDestinationChangedBehaviours)

		_previousDestination = destination
	}

	fun onBackgrounded() = executeBehaviours(onBackgroundedBehaviours)


	private fun executeBehaviours(behaviours: Collection<NavigationBehaviour>)
	{
		for (behaviour in behaviours) when (behaviour)
		{
			is DestinationBehaviour ->
			{
				if (_currentDestination?.id in behaviour.destinations)
				{
					behaviour.ifCurrentDestinationIsInList?.invoke()
				}
				else behaviour.ifCurrentDestinationIsNotInList?.invoke()
			}
			is ActionBehaviour      ->
			{
				if (behaviour.actions.any { it.first == _previousDestination?.id && it.second == _currentDestination?.id })
				{
					behaviour.ifCurrentActionIsInList?.invoke()
				}
				else behaviour.ifCurrentActionIsNotInList?.invoke()
			}
		}
	}
}

sealed interface NavigationBehaviour

data class DestinationBehaviour(
	val destinations: Collection<Int>,
	val ifCurrentDestinationIsInList: (() -> Unit)? = null,
	val ifCurrentDestinationIsNotInList: (() -> Unit)? = null,
) : NavigationBehaviour

data class ActionBehaviour(
	val actions: Collection<Pair<Int, Int>>,
	val ifCurrentActionIsInList: (() -> Unit)? = null,
	val ifCurrentActionIsNotInList: (() -> Unit)? = null,
) : NavigationBehaviour