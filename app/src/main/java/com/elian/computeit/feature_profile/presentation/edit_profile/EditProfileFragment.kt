package com.elian.computeit.feature_profile.presentation.edit_profile

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.getUsernameErrorMessage
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.core.util.extensions.toBytes
import com.elian.computeit.core.util.extensions.trimWhitespacesBeforeNewLine
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

	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent())
	{
		val imageBytes = it?.toBytes(context) ?: byteArrayOf()

		viewModel.onAction(EnterProfilePic(imageBytes.toList()))

		binding.sivProfilePic.setImageURI(null)
		binding.sivProfilePic.setImageURI(it)
	}
	private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission())
	{
		if (!it) return@registerForActivityResult

		getContent.launch("image/*")
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initUi()
	}


	private fun initUi() = binding.apply2()
	{
		tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
		tietBiography.addTextChangedListener { viewModel.onAction(EnterBiography("$it".trim().trimWhitespacesBeforeNewLine())) }

		viewModel.profilePicState.value.also { if (it.isNotEmpty()) viewModel.onAction(EnterProfilePic(it)) }

		sivProfilePic.setOnClickListener()
		{
			requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
		}

		btnSave.setOnClickListener { viewModel.onAction(Save) }
	}

	private fun subscribeToEvents() = viewModel.apply2()
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
		}
	}
}