package com.elian.computeit.feature_profile.presentation.private_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.extensions.navigateTo
import com.elian.computeit.core.presentation.util.extensions.setImageBytes
import com.elian.computeit.core.presentation.util.extensions.showAlertDialog
import com.elian.computeit.core.presentation.util.extensions.text2
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentPrivateProfileBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_profile.presentation.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrivateProfileFragment : Fragment(R.layout.fragment_private_profile) {

	private val viewModel by activityViewModels<ProfileViewModel>()
	private val binding by viewBinding(FragmentPrivateProfileBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi() = using(binding) {
		btnEdit.setOnClickListener {
			navigate(R.id.action_privateProfileFragment_to_editProfileFragment)
		}
		btnLogout.setOnClickListener {
			showAlertDialog(
				messageResId = R.string.alert_dialog_are_you_sure_you_want_to_log_out,
				onPositiveClick = {
					lifecycleScope.launch {
						viewModel.logout()
						navigateTo<LoginActivity>()
					}
				}
			)
		}
	}

	private fun subscribeToEvents() = using(viewModel) {
		collectLatestFlowWhenStarted(sharedState.filterNotNull()) {
			binding.apply {
				tvUsername.text2 = "@${it.username}"
				tvBiography.text2 = it.biography

				if (it.profilePicBytes.isNotEmpty()) {
					sivProfilePic.setImageBytes(it.profilePicBytes.toByteArray())
				}
			}
		}
		collectLatestFlowWhenStarted(privateProfileState.filterNotNull()) {
			binding.apply {
				it.createdAt?.also { createdAt ->
					tvCreatedAt.text2 = getString(R.string.feature_profile_account_created_at_PH).format(createdAt)
				}

				pbIsLoading.isVisible = it.isLoading
				btnEdit.isGone = it.isLoading
				tvLabelBiography.isGone = it.isLoading
				btnLogout.isGone = it.isLoading
				sivProfilePic.isInvisible = it.isLoading
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()

		requireActivity().viewModelStore.clear()
	}
}