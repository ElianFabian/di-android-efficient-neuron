package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.ActionEvent
import com.elian.computeit.core.presentation.util.DestinationEvent
import com.elian.computeit.core.presentation.util.NavigationManager
import com.elian.computeit.databinding.FragmentTestDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

	private val navController by lazy { findNavController(R.id.navHostFragment) }

	private var _isNavigateUpEnabled = true
	private var _defaultSoftInputMode: Int? = null

	private var _previousFragment: Fragment? = null

	private val _navigationManager = NavigationManager(
		onDestinationChangedEvents = setOf(
			// Adjust layout when keyboard is open
			DestinationEvent(
				destinations = setOf(
					R.id.editProfileFragment,
				),
				onEvent = { window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) },
				onOtherEvent = { window.setSoftInputMode(_defaultSoftInputMode!!) },
			),
			// Disable navigate up
			ActionEvent(
				actions = setOf(
					R.id.testFragment to R.id.testDetailsFragment,
				),
				onEvent = { _isNavigateUpEnabled = false },
				onOtherEvent = { _isNavigateUpEnabled = true },
			),
		),
		onBackgroundedEvents = setOf(
			// Navigate up
			DestinationEvent(
				destinations = setOf(
					R.id.testFragment,
				),
				onEvent = { navController.navigateUp() },
			),
		),
	)


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		initializeData()
		initializeLogic()
	}

	override fun onPause() {
		super.onPause()

		_navigationManager.onBackgrounded()
	}

	override fun onBackPressed() {
		when {
			navController.currentDestination?.id == R.id.homeFragment -> finish()
			_isNavigateUpEnabled                                      -> navController.navigateUp()
		}
	}


	fun onFragmentViewCreated(fragmentBinding: ViewBinding) {
		when (fragmentBinding) {
			is FragmentTestDetailsBinding -> {
				if (_previousFragment !is HomeFragment) return

				fragmentBinding.btnContinue.isGone = true
			}
		}
	}


	private fun initializeData() {
		_defaultSoftInputMode = window.attributes.softInputMode
	}

	private fun initializeLogic() {
		navController.addOnDestinationChangedListener { _, destination, _ ->

			_navigationManager.onDestinationChanged(destination)

			_previousFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)?.childFragmentManager?.primaryNavigationFragment
		}
	}
}
