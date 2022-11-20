package com.elian.computeit.core.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.elian.computeit.R
import com.elian.computeit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener
{
    private lateinit var binding: ActivityMainBinding

    private var currentFragment: Fragment? = null
    private var _isNavigateUpEnable = true


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    override fun onBackPressed()
    {
        when
        {
            currentFragment is HomeFragment -> finish()
            _isNavigateUpEnable             -> findNavController(R.id.navHostFragment).navigateUp()
        }
    }


    private fun initUi()
    {
        findNavController(R.id.navHostFragment).addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?)
    {
        currentFragment = if (destination.id == R.id.homeFragment)
        {
            HomeFragment()
        }
        else null

        if (destination.id == R.id.testEndFragment)
        {
            _isNavigateUpEnable = false
            return
        }

        _isNavigateUpEnable = true
    }
}