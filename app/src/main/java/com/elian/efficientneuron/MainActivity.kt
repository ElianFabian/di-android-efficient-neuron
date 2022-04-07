package com.elian.efficientneuron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.elian.efficientneuron.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity()//, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityMainBinding

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
    
    private fun initUI()
    {
        //binding.navigationView.setNavigationItemSelectedListener(this)
    }
    
    // TODO: implement navigation with navigation view

//    override fun onNavigationItemSelected(item: MenuItem): Boolean
//    {
//        when(item.itemId)
//        {
//            
//        }
//    }
}