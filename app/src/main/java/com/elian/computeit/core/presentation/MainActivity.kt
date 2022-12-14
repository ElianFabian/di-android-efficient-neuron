package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.ActionBehaviour
import com.elian.computeit.core.presentation.util.DestinationBehaviour
import com.elian.computeit.core.presentation.util.NavigationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main)
{
	private val navController by lazy { findNavController(R.id.navHostFragment) }

	private var _isNavigateUpEnabled = true
	private var _defaultSoftInputMode: Int? = null

	private val _navigationManager = NavigationManager(
		onDestinationChangedBehaviours = setOf(
			// Adjust layout when keyboard is open
			DestinationBehaviour(
				destinations = setOf(
					R.id.editProfileFragment, R.id.testConfigurationFragment,
				),
				ifCurrentDestinationIsInList = { window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) },
				ifCurrentDestinationIsNotInList = { window.setSoftInputMode(_defaultSoftInputMode!!) },
			),
			// Disable navigate up
			ActionBehaviour(
				actions = setOf(
					R.id.testFragment to R.id.testDetailsFragment,
				),
				ifCurrentActionIsInList = { _isNavigateUpEnabled = false },
				ifCurrentActionIsNotInList = { _isNavigateUpEnabled = true }
			),
		),
		onBackgroundedBehaviours = setOf(
			// Navigate up
			DestinationBehaviour(
				destinations = setOf(
					R.id.testFragment,
				),
				ifCurrentDestinationIsInList = { navController.navigateUp() },
			),
		),
	)


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		initData()
		initLogic()
	}

	override fun onPause()
	{
		super.onPause()

		_navigationManager.onBackgrounded()
	}

	override fun onBackPressed()
	{
		when
		{
			navController.currentDestination?.id == R.id.homeFragment -> finish()
			_isNavigateUpEnabled                                      -> navController.navigateUp()
		}
	}


	private fun initData()
	{
		_defaultSoftInputMode = window.attributes.softInputMode
	}

	private fun initLogic()
	{
		navController.addOnDestinationChangedListener { _, destination, _ ->

			_navigationManager.onDestinationChanged(destination)
		}
	}
}