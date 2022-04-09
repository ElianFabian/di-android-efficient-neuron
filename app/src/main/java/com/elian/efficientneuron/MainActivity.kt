package com.elian.efficientneuron

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.databinding.ActivityMainBinding
import com.elian.efficientneuron.extension.toast
import com.elian.efficientneuron.ui.aboutus.AboutUsFragment
import com.elian.efficientneuron.ui.functions.FunctionsFragment
import com.elian.efficientneuron.ui.profile.ProfileFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var currentFragment: Fragment

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (toggle.onOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?)
    {
        super.onPostCreate(savedInstanceState)

        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig)

        toggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed()
    {
        // Goes to Home Fragment unless we're in Home Fragment, other wise exits the app
        if (currentFragment is FunctionsFragment)
        {
            super.onBackPressed()
        }
        else goToFragment(FunctionsFragment())
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun goToFragment(fragment: Fragment)
    {
        currentFragment = fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }
    }

    //endregion

    //region NavigationView.OnNavigationItemSelectedListener

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.navHome       -> goToFragment(FunctionsFragment())
            R.id.navProfile    -> goToFragment(ProfileFragment())
            R.id.navTips       -> toast("No yet implemented.")
            R.id.navStatistics -> toast("No yet implemented.")
            R.id.navSettings   -> toast("No yet implemented.")
            R.id.navAboutUs    -> goToFragment(AboutUsFragment())
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    //endregion
}

