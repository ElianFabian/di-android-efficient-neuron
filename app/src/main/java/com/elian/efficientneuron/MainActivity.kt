package com.elian.efficientneuron

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.databinding.ActivityMainBinding
import com.elian.efficientneuron.util.extension.toast
import com.elian.efficientneuron.ui.aboutus.AboutUsFragment
import com.elian.efficientneuron.ui.operations.OperationsFragment
import com.elian.efficientneuron.ui.profile.ProfileFragment
import com.elian.efficientneuron.ui.statistics.StatisticsFragment
import com.elian.efficientneuron.ui.tiplist.TipListFragment
import com.elian.efficientneuron.ui.tipmanager.TipManagerFragment
import com.elian.efficientneuron.util.extension.goToFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var currentFragmentItem: Fragment

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    // styles.xml/frgGame_ibButtons/android:onClick
    fun animation_onClick(view: View)
    {
        val scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down)
        val scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)

        view.startAnimation(scaleDown)
        view.postOnAnimation { view.startAnimation(scaleUp) }
    }

    //endregion

    //region NavigationView.OnNavigationItemSelectedListener

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        currentFragmentItem = when (item.itemId)
        {
            R.id.navHome       -> goToFragment(OperationsFragment())
            R.id.navProfile    -> goToFragment(ProfileFragment())
            R.id.navTips       -> goToFragment(TipListFragment())
            R.id.navStatistics -> goToFragment(StatisticsFragment())
            //R.id.navSettings   ->  toast("No yet implemented.")
            R.id.navAboutUs    -> goToFragment(AboutUsFragment())

            else               -> Fragment()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    //endregion
}

