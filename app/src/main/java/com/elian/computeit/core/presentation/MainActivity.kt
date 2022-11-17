package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.NavigationDrawerFragmentTag
import com.elian.computeit.core.presentation.util.extensions.goToFragment
import com.elian.computeit.databinding.ActivityMainBinding
import com.elian.computeit.feature_tips.presentation.tip_list.TipListFragment
import com.elian.computeit.ui.AboutUsFragment
import com.elian.computeit.ui.ProfileFragment
import com.elian.computeit.ui.SettingsFragment
import com.elian.computeit.ui.StatisticsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
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
        enableDrawerLayout()

        when
        {
            currentFragment is HomeFragment                -> finish()
            currentFragment is NavigationDrawerFragmentTag -> goTo(HomeFragment())
            _isNavigateUpEnable                            -> findNavController(R.id.navHostFragment).navigateUp()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun disableDrawerLayout() = binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    private fun enableDrawerLayout() = binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    private fun initUi()
    {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        findNavController(R.id.navHostFragment).addOnDestinationChangedListener(this)

        initMenuItemListener()
    }

    private fun initMenuItemListener() = binding.navigationView.setNavigationItemSelectedListener()
    {
        when (it.itemId)
        {
            R.id.navHome       -> goTo(HomeFragment())
            R.id.navProfile    -> goTo(ProfileFragment())
            R.id.navTips       -> goTo(TipListFragment())
            R.id.navStatistics -> goTo(StatisticsFragment())
            R.id.navSettings   -> goTo(SettingsFragment())
            R.id.navAboutUs    -> goTo(AboutUsFragment())

            else               -> error("Trying to go to a non-existing fragment.")
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        true
    }

    private fun goTo(fragment: Fragment, args: Bundle? = null)
    {
        currentFragment = goToFragment(fragment, args)
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