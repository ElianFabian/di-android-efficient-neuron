package com.elian.computeit.feature_profile.presentation.edit_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.getUsernameErrorMessage
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.toBitmap
import com.elian.computeit.core.util.extensions.trimWhitespacesBeforeNewLine
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentEditProfileBinding
import com.elian.computeit.feature_profile.presentation.ProfileViewModel
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile)
{
	private val viewModel by activityViewModels<ProfileViewModel>()
	private val binding by viewBinding(FragmentEditProfileBinding::bind)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi() = using(binding)
	{
		tietUsername.allowMultilineAndDisableEnterNewLine()

		tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
		tietBiography.addTextChangedListener { viewModel.onAction(EnterBiography("$it".trim().trimWhitespacesBeforeNewLine())) }

		viewModel.sharedState.value?.profilePicBytes?.also { if (it.isNotEmpty()) viewModel.onAction(EnterProfilePic(it)) }

		val dialog = ChooseOrDeleteProfilePictureBottomDialog()

		dialog.setOnEventListener<ChooseOrDeleteProfilePictureBottomDialog.Event.OnDeleteImage>(this@EditProfileFragment) {
			viewModel.onAction(EnterProfilePic(emptyList()))
		}
		dialog.setOnEventListener(this@EditProfileFragment) { event: ChooseOrDeleteProfilePictureBottomDialog.Event.OnPictureSelected ->
			val compressedImageBytes = event.pictureUri.toBitmap(context)
				.reduceSize(102400)
				.cropToSquare()
				.toBytes()

			viewModel.onAction(EnterProfilePic(compressedImageBytes.toList()))
		}

		sivProfilePic.setOnClickListener()
		{
			dialog.show(parentFragmentManager)
		}

		btnSave.setOnClickListener { viewModel.onAction(Save) }
	}

	private fun subscribeToEvents() = using(viewModel)
	{
		collectLatestFlowWhenStarted(sharedState.filterNotNull())
		{
			binding.apply()
			{
				tietUsername.text2 = it.username
				tietBiography.text2 = it.biography
				sivProfilePic.setImageBytes(it.profilePicBytes.toByteArray())
			}
		}
		collectLatestFlowWhenStarted(editProfileState)
		{
			binding.apply()
			{
				tilUsername.error2 = getUsernameErrorMessage(context, it.usernameError)
				lpiIsLoading.isGone = !it.isLoading
				btnSave.isEnabled = !it.isLoading
				sivProfilePic.setImageBytes(it.profilePicBytes.toByteArray())
			}
		}
		collectFlowWhenStarted(editProfileEventFlow)
		{
			when (it)
			{
				is EditProfileEvent.OnSave             -> showToast(R.string.message_info_successfully_updated)
				is EditProfileEvent.OnShowErrorMessage -> showToast(it.error.asString(context))
			}
		}
	}
}