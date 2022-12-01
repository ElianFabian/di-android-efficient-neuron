package com.elian.computeit.feature_profile.presentation.private_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.extensions.navigateTo
import com.elian.computeit.core.presentation.util.extensions.text2
import com.elian.computeit.core.presentation.util.showAlertDialog
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentPrivateProfileBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrivateProfileFragment : Fragment(R.layout.fragment_private_profile)
{
	private val viewModel by viewModels<PrivateProfileViewModel>()
	private val binding by viewBinding(FragmentPrivateProfileBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		initUi()
	}


	private fun initUi() = binding.apply2()
	{
		lifecycleScope.launch()
		{
			val info = viewModel.getProfileInfo()

			tvUsername.text2 = info.username
			tvBiography.text2 = info.biography
			tvCreatedAt.text2 = getString(R.string.profile_account_created_at_ph).format(info.createdAt)
		}

		btnEdit.setOnClickListener { navigate(R.id.action_profileFragment_to_editProfileFragment) }
		btnLogout.setOnClickListener()
		{
			showAlertDialog(
				messageResId = R.string.alert_dialog_are_you_sure_you_want_to_log_out,
				onPositiveClick = {
					lifecycleScope.launch()
					{
						viewModel.logout()
						navigateTo<LoginActivity>()
					}
				}
			)
		}
	}
}