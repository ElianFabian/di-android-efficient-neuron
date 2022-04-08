package com.elian.efficientneuron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.databinding.ActivityMainBinding
import com.elian.efficientneuron.ui.profile.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initUI()
    }

    override fun onStart()
    {
        super.onStart()

        initUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (toggle.onOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(::setNavigation)

        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setNavigation(item: MenuItem): Boolean
    {
        Toast.makeText(this, "uwu", Toast.LENGTH_SHORT).show()
        when (item.itemId)
        {
            R.id.navProfile -> goToFragment(ProfileFragment())
        }
        return true
    }

    private fun goToFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }
    }

    //endregion

    //region NavigationView.OnNavigationItemSelectedListener

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        Toast.makeText(this, "uwu", Toast.LENGTH_SHORT).show()
        when (item.itemId)
        {
            R.id.navProfile -> goToFragment(ProfileFragment())
        }
        return true
    }
}

//endregion
