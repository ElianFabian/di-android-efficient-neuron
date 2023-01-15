package com.elian.computeit.feature_profile.presentation.private_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentPrivateProfileBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_profile.presentation.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
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


	private fun initUi() = using(binding)
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

	private fun subscribeToEvents() = using(viewModel)
	{
		collectLatestFlowWhenStarted(profileState.filterNotNull())
		{
			binding.apply()
			{
				tvUsername.text2 = "@${it.username}"
				tvBiography.text2 = it.biography
				tvCreatedAt.text2 = getString(R.string.feature_profile_account_created_at_PH).format(it.createdAt)
				sivProfilePic.setImageBytes(it.profilePicBytes.toByteArray())
			}
		}
		collectLatestFlowWhenStarted(privateProfileIsLoadingState) 
		{
			binding.apply()
			{
				pbIsLoading.isVisible = it
				btnEdit.isGone = it
				tvLabelBiography.isGone = it
				btnLogout.isGone = it
				sivProfilePic.isVisible = !it
			}
		}
	}
}