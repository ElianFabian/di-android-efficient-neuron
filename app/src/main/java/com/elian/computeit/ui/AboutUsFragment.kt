package com.elian.computeit.ui


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import mehdi.sakout.aboutpage.AboutPage

// https://camposha.info/android-examples/android-about-us-page-libraries
class AboutUsFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?
    {
        simulateDayNight( /* DAY */1)
//        val adsElement = Element()
//        adsElement.setTitle("Advertise with us")

        return AboutPage(context)
            .isRTL(false)
            .setDescription(getString(R.string.frgAboutUs_description))
            //.addItem(adsElement)
            .addGitHub("ElianFabian/ComputeIt", getString(R.string.frgAboutUs_github_title))
            //.addItem(copyRightsElement)
            .create()
    }

//    val copyRightsElement: Element
//        get()
//        {
//            val copyRightsElement = Element()
//
//            copyRightsElement.iconTint = R.color.about_item_icon_color
//            copyRightsElement.iconNightTint = android.R.color.white
//            copyRightsElement.gravity = Gravity.CENTER
//            copyRightsElement.onClickListener = View.OnClickListener {
//                Toast.makeText(
//                    context,
//                    "copyrights",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            return copyRightsElement
//        }

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