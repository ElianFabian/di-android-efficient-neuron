package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.presentation.MainActivityEvent.OnUserNotLoggedIn
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.goToFragment
import com.elian.computeit.core.util.extensions.navigateTo
import com.elian.computeit.databinding.ActivityMainBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_tips.presentation.tip_list.TipListFragment
import com.elian.computeit.ui.AboutUsFragment
import com.elian.computeit.ui.ProfileFragment
import com.elian.computeit.ui.SettingsFragment
import com.elian.computeit.ui.StatisticsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private var currentFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        subscribeToEvents()

        viewModel.tryLogin()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initData()
    }

    override fun onBackPressed()
    {
        enableDrawerLayout()

        // Goes to Home Fragment unless we are already in, other wise exits the app
        if (currentFragment is HomeFragment)
        {
            onBackPressedDispatcher.onBackPressed()
        }
        else
        {
            findNavController(R.id.nav_host_fragment).navigateUp()
            goToFragmentAndSetCurrent(HomeFragment())
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

        initMenuItemListener()
    }

    private fun initData()
    {
        currentFragment = HomeFragment()
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is OnUserNotLoggedIn -> navigateTo<LoginActivity>()
            }
        }
    }

    private fun initMenuItemListener() = binding.navigationView.setNavigationItemSelectedListener()
    {
        when (it.itemId)
        {
            R.id.navHome       -> goToFragmentAndSetCurrent(HomeFragment())
            R.id.navProfile    -> goToFragmentAndSetCurrent(ProfileFragment())
            R.id.navTips       -> goToFragmentAndSetCurrent(TipListFragment())
            R.id.navStatistics -> goToFragmentAndSetCurrent(StatisticsFragment())
            R.id.navSettings   -> goToFragmentAndSetCurrent(SettingsFragment())
            R.id.navAboutUs    -> goToFragmentAndSetCurrent(AboutUsFragment())

            else               -> error("Trying to go to a non-existing fragment.")
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        true
    }

    private fun goToFragmentAndSetCurrent(fragment: Fragment, args: Bundle? = null)
    {
        currentFragment = goToFragment(fragment, args)
    }
}