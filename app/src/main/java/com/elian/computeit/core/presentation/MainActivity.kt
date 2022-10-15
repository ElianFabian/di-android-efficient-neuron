package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.data.database.AppDatabase
import com.elian.computeit.databinding.ActivityMainBinding
import com.elian.computeit.ui.SettingsFragment
import com.elian.computeit.ui.AboutUsFragment
import com.elian.computeit.ui.OperationsFragment
import com.elian.computeit.ui.ProfileFragment
import com.elian.computeit.ui.StatisticsFragment
import com.elian.computeit.ui.TipListFragment
import com.elian.computeit.core.util.extensions.goToFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var currentFragmentItem: Fragment? = null

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppDatabase.create(this)

        initUI()
    }

    override fun onBackPressed()
    {
        // Goes to Home Fragment unless we are already, other wise exits the app
        if (currentFragmentItem is OperationsFragment)
        {
            super.onBackPressed()
        }
        else goToFragment(OperationsFragment())
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

    private fun initUI()
    {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        
        initMenuItemListener()
    }
    
    private fun initMenuItemListener() = binding.navigationView.setNavigationItemSelectedListener() 
    {
        currentFragmentItem = when (it.itemId)
        {
            R.id.navHome       -> goToFragment(OperationsFragment())
            R.id.navProfile    -> goToFragment(ProfileFragment())
            R.id.navTips       -> goToFragment(TipListFragment())
            R.id.navStatistics -> goToFragment(StatisticsFragment())
            R.id.navSettings   -> goToFragment(SettingsFragment())
            R.id.navAboutUs    -> goToFragment(AboutUsFragment())

            else               -> Fragment()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        
        true
    }

    //endregion
}

