package com.elian.efficientneuron.ui.aboutus


import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import mehdi.sakout.aboutpage.R

// https://camposha.info/android-examples/android-about-us-page-libraries
class AboutUsFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        simulateDayNight( /* DAY */1)
//        val adsElement = Element()
//        adsElement.setTitle("Advertise with us")
        val aboutPage = AboutPage(context)
            .isRTL(false)
            .setDescription(getString(com.elian.efficientneuron.R.string.frgAboutUs_description))
            //.addItem(adsElement)
            .addEmail("elian.fabian.el.belaizi@gmail.com")
            .addGitHub("ElianFabian")
            //.addItem(copyRightsElement)
            .create()

        return aboutPage
    }

    val copyRightsElement: Element
        get()
        {
            val copyRightsElement = Element()

            copyRightsElement.iconTint = R.color.about_item_icon_color
            copyRightsElement.iconNightTint = android.R.color.white
            copyRightsElement.gravity = Gravity.CENTER
            copyRightsElement.onClickListener = View.OnClickListener {
                Toast.makeText(
                    context,
                    "copyrights",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return copyRightsElement
        }

    fun simulateDayNight(currentSetting: Int)
    {
        val DAY = 0
        val NIGHT = 1
        val FOLLOW_SYSTEM = 3
        val currentNightMode = (resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK)
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO)
        {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES)
        {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }
        else if (currentSetting == FOLLOW_SYSTEM)
        {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }
}