package com.elian.computeit.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.util.extensions.isUserLoggedIn
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.presentation.util.extensions.navigateTo
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InitialActivity : AppCompatActivity()
{
	@Inject
	lateinit var appData: LocalAppDataRepository


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		lifecycleScope.launch()
		{
			if (appData.isUserLoggedIn())
			{
				navigateTo<MainActivity>()
			}
			else navigateTo<LoginActivity>()
		}
	}
}