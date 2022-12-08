package com.elian.computeit.core.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.elian.computeit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main)
{
	private val navController by lazy { findNavController(R.id.navHostFragment) }

	private val _disabledNavigateUpDestinations = setOf(
		R.id.testFragment to R.id.testDetailsFragment,
	)
	private val _onBackgroundedNavigateUpDestinations = setOf(
		R.id.testFragment,
	)

	private var _isNavigateUpEnable = true
	private var _previousDestination: NavDestination? = null
	private lateinit var _currentDestination: NavDestination


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		initLogic()
	}

	override fun onPause()
	{
		super.onPause()

		onBackgrounded()
	}

	override fun onBackPressed()
	{
		when
		{
			_currentDestination.id == R.id.homeFragment -> finish()
			_isNavigateUpEnable                         -> navController.navigateUp()
		}
	}


	private fun initLogic()
	{
		navController.addOnDestinationChangedListener { controller, destination, arguments ->

			_currentDestination = destination
			
			onDestinationChangedListener(controller, destination, arguments)

			_previousDestination = destination
		}
	}
	
	private fun onDestinationChangedListener(controller: NavController, destination: NavDestination, arguments: Bundle?)
	{
		_isNavigateUpEnable = !_disabledNavigateUpDestinations.any { it.first == _previousDestination?.id && it.second == destination.id }
	}

	private fun onBackgrounded()
	{
		if (_currentDestination.id in _onBackgroundedNavigateUpDestinations) navController.navigateUp()
	}
}