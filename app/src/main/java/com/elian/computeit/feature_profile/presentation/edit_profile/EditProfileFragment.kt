package com.elian.computeit.feature_profile.presentation.edit_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.allowMultilineAndDisableEnterNewLine
import com.elian.computeit.core.presentation.util.extensions.collectFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.cropToSquare
import com.elian.computeit.core.presentation.util.extensions.error2
import com.elian.computeit.core.presentation.util.extensions.reduceSize
import com.elian.computeit.core.presentation.util.extensions.setImageBytes
import com.elian.computeit.core.presentation.util.extensions.showToast
import com.elian.computeit.core.presentation.util.extensions.text2
import com.elian.computeit.core.presentation.util.extensions.toBytes
import com.elian.computeit.core.presentation.util.getUsernameErrorMessage
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.toBitmap
import com.elian.computeit.core.util.extensions.trimWhitespacesBeforeNewLine
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentEditProfileBinding
import com.elian.computeit.feature_profile.presentation.ProfileViewModel
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.EnterBiography
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.EnterProfilePic
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.EnterUsername
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.Save
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

	private val viewModel by activityViewModels<ProfileViewModel>()
	private val binding by viewBinding(FragmentEditProfileBinding::bind)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi() = using(binding) {
		tietUsername.allowMultilineAndDisableEnterNewLine()

		tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
		tietBiography.addTextChangedListener { viewModel.onAction(EnterBiography("$it".trim().trimWhitespacesBeforeNewLine())) }

		viewModel.sharedState.value?.profilePicBytes?.also { if (it.isNotEmpty()) viewModel.onAction(EnterProfilePic(it)) }

		val dialog = ChooseOrDeleteProfilePictureBottomDialog.newInstance()

		dialog.setEventListener(this@EditProfileFragment) { event ->
			when (event) {
				is ChooseOrDeleteProfilePictureBottomDialog.Event.OnDeleteImage     -> {
					viewModel.onAction(EnterProfilePic(emptyList()))
				}
				is ChooseOrDeleteProfilePictureBottomDialog.Event.OnPictureSelected -> {
					val compressedImageBytes = event.pictureUri.toBitmap(context)
						.reduceSize(102400)
						.cropToSquare()
						.toBytes()

					viewModel.onAction(EnterProfilePic(compressedImageBytes.toList()))
				}
			}
		}

		sivProfilePic.setOnClickListener {
			dialog.show(this@EditProfileFragment)
		}

		btnSave.setOnClickListener { viewModel.onAction(Save) }
	}

	private fun subscribeToEvents() = using(viewModel) {
		collectLatestFlowWhenStarted(sharedState.filterNotNull()) {
			binding.apply {
				tietUsername.text2 = it.username
				tietBiography.text2 = it.biography
				sivProfilePic.setImageBytes(it.profilePicBytes.toByteArray())
			}
		}
		collectLatestFlowWhenStarted(editProfileState) {
			binding.apply {
				tilUsername.error2 = getUsernameErrorMessage(context, it.usernameError)
				lpiIsLoading.isGone = !it.isLoading
				btnSave.isEnabled = !it.isLoading
				sivProfilePic.setImageBytes(it.profilePicBytes.toByteArray())
			}
		}
		collectFlowWhenStarted(editProfileEventFlow) {
			when (it) {
				is EditProfileEvent.OnSave             -> showToast(R.string.message_info_successfully_updated)
				is EditProfileEvent.OnShowErrorMessage -> showToast(it.error.asString(context))
			}
		}
	}
}