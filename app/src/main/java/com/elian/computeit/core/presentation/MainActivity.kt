package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.goToFragment
import com.elian.computeit.core.util.extensions.navigateTo
import com.elian.computeit.data.database.AppDatabase
import com.elian.computeit.databinding.ActivityMainBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.ui.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()

    private lateinit var toggle: ActionBarDrawerToggle
    private var currentFragmentItem: Fragment? = null

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        subscribeToEvents()

        viewModel.tryLoginUser()

        AppDatabase.create(this)

        initUi()
        init()
    }

    override fun onBackPressed()
    {
        enableDrawerLayout()

        // Goes to Home Fragment unless we are already in, other wise exits the app
        if (currentFragmentItem is OperationsFragment)
        {
            onBackPressedDispatcher.onBackPressed()
        }
        else
        {
            findNavController(R.id.nav_host_fragment).navigateUp()
            goToFragmentAndSetCurrent(OperationsFragment())
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

    //endregion

    //region Methods

    fun disableDrawerLayout() = binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    private fun enableDrawerLayout() = binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    private fun initUi()
    {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        initMenuItemListener()
    }

    private fun init()
    {
        lifecycleScope.launch()
        {
            currentFragmentItem = OperationsFragment()
        }
    }

    private fun subscribeToEvents()
    {
        lifecycleScope.launchWhenStarted()
        {
            viewModel.eventFlow.collect()
            {
                when (it)
                {
                    is MainActivityEvent.OnUserNotRegistered -> navigateTo<LoginActivity>()
                }
            }
        }
    }

    private fun initMenuItemListener() = binding.navigationView.setNavigationItemSelectedListener()
    {
        when (it.itemId)
        {
            R.id.navHome       -> goToFragmentAndSetCurrent(OperationsFragment())
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
        currentFragmentItem = goToFragment(fragment, args)
    }

    //endregion
}