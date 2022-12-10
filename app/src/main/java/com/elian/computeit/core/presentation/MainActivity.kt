package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.WindowManager
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
	private val _adjustResizeDestinations = setOf(
		R.id.editProfileFragment
	)

	private var _isNavigateUpEnable = true
	private var _previousDestination: NavDestination? = null
	private lateinit var _currentDestination: NavDestination

	private var _defaultSoftInputMode: Int? = null


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		initData()
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


	private fun initData()
	{
		_defaultSoftInputMode = window.attributes.softInputMode
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

		if (destination.id in _adjustResizeDestinations)
		{
			// Adjust layout when keyboard is open
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
		}
		else window.setSoftInputMode(_defaultSoftInputMode!!)
	}

	private fun onBackgrounded()
	{
		if (_currentDestination.id in _onBackgroundedNavigateUpDestinations) navController.navigateUp()
	}
}