package com.elian.computeit.feature_profile.presentation.edit_profile

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnSave
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnShowErrorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile)
{
	private val viewModel by activityViewModels<ProfileViewModel>()
	private val binding by viewBinding(FragmentEditProfileBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initUi()
	}


	private fun initUi() = using(binding)
	{
		tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
		tietBiography.addTextChangedListener { viewModel.onAction(EnterBiography("$it".trim().trimWhitespacesBeforeNewLine())) }

		// This is to set the desired behaviour when typing
		tietUsername.setRawInputType(InputType.TYPE_CLASS_TEXT)

		viewModel.profilePicState.value.also { if (it.isNotEmpty()) viewModel.onAction(EnterProfilePic(it)) }

		sivProfilePic.setOnClickListener()
		{
			ChooseOrDeleteProfilePictureBottomDialog(
				onPictureSelected = {
					val compressedImageBytes = it?.toBitmap(context)
						?.reduceSize(102400)
						?.cropToSquare()
						?.toBytes() ?: byteArrayOf()

					viewModel.onAction(EnterProfilePic(compressedImageBytes.toList()))

					binding.sivProfilePic.setImageURI(it)
				},
				onDeleteImage = {
					sivProfilePic.setImageBytes(byteArrayOf())
					viewModel.onAction(EnterProfilePic(emptyList()))
				},
			).show(requireActivity().supportFragmentManager, "")
		}

		btnSave.setOnClickListener { viewModel.onAction(Save) }
	}

	private fun subscribeToEvents() = using(viewModel)
	{
		collectFlowWhenStarted(editProfileEventFlow)
		{
			when (it)
			{
				is OnSave             -> showToast(R.string.message_info_successfully_updated)
				is OnShowErrorMessage -> showToast(it.error.asString(context))
			}
		}
		collectFlowWhenStarted(profilePicState) { binding.sivProfilePic.setImageBytes(it.toByteArray()) }
		collectFlowWhenStarted(usernameState) { binding.tietUsername.setText(it) }
		collectFlowWhenStarted(biographyState) { binding.tietBiography.setText(it) }
		collectLatestFlowWhenStarted(editProfileState)
		{
			binding.tilUsername.error2 = getUsernameErrorMessage(context, it.usernameField.error)

			binding.lpiIsLoading.isGone = !it.isLoading
			binding.btnSave.isEnabled = !it.isLoading
		}
	}
}