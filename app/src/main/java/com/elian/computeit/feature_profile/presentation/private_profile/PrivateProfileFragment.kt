package com.elian.computeit.feature_profile.presentation.private_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.showAlertDialog
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentPrivateProfileBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_profile.presentation.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrivateProfileFragment : Fragment(R.layout.fragment_private_profile)
{
	private val viewModel by activityViewModels<ProfileViewModel>()
	private val binding by viewBinding(FragmentPrivateProfileBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initUi()
	}


	private fun initUi() = binding.apply2()
	{
		btnEdit.setOnClickListener()
		{
			navigate(R.id.action_privateProfileFragment_to_editProfileFragment)
		}

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

	private fun subscribeToEvents() = viewModel.apply2()
	{
		collectLatestFlowWhenStarted(profilePicState.filter { it.isNotEmpty() })
		{
			binding.sivProfilePic.setImageBytes(it.toByteArray())
		}
		collectLatestFlowWhenStarted(createdAtState.filter { it.isNotBlank() })
		{
			binding.tvCreatedAt.text2 = getString(R.string.feature_profile_account_created_at_PH).format(it)
		}
		collectLatestFlowWhenStarted(usernameState)
		{
			binding.tvUsername.text2 = it
		}
		collectLatestFlowWhenStarted(privateProfileViewsAreGoneState)
		{
			binding.btnEdit.isGone = it
			binding.tvLabelBiography.isGone = it
			binding.btnLogout.isGone = it

			if (!it) binding.sivProfilePic.startAlphaAnimation(
				fromAlpha = 0F,
				toAlpha = 1F,
				durationMillis = 200,
			)
		}
		collectLatestFlowWhenStarted(biographyState) { binding.tvBiography.text2 = it }
	}
}