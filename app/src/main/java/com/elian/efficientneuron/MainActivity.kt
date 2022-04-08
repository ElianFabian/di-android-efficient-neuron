package com.elian.efficientneuron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity()//, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toogle: ActionBarDrawerToggle

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart()
    {
        super.onStart()

        initUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (toogle.onOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(::setNavigation)

        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setNavigation(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            //R.id.navProfile -> goToFragment()
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


    // TODO: implement navigation with navigation view

//    override fun onNavigationItemSelected(item: MenuItem): Boolean
//    {
//        when(item.itemId)
//        {
//            
//        }
//    }
}