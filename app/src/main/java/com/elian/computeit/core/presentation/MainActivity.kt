package com.elian.computeit.core.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.elian.computeit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main)
{
	private val navController by lazy { findNavController(R.id.navHostFragment) }

	private val _disabledNavigateUpDestinations = setOf(
		R.id.testDetailsFragment,
	)
	private val _onBackgroundedNavigateUpDestinations = setOf(
		R.id.testFragment,
	)
	private var _isNavigateUpEnable = true
	private var _currentFragment: Fragment? = null
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
			_currentFragment is HomeFragment -> finish()
			_isNavigateUpEnable              -> navController.navigateUp()
		}
	}


	private fun initLogic()
	{
		navController.addOnDestinationChangedListener { _, destination, _ ->

			_currentDestination = destination

			_currentFragment = if (destination.id == R.id.homeFragment) HomeFragment() else null

			_isNavigateUpEnable = destination.id !in _disabledNavigateUpDestinations
		}
	}

	private fun onBackgrounded()
	{
		if (_currentDestination.id in _onBackgroundedNavigateUpDestinations) navController.navigateUp()
	}
}