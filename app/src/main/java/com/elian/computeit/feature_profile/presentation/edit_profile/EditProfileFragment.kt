package com.elian.computeit.feature_profile.presentation.edit_profile

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.collectFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.error2
import com.elian.computeit.core.presentation.util.extensions.toast
import com.elian.computeit.core.presentation.util.getUsernameErrorMessage
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.core.util.extensions.trimWhitespacesBeforeNewLine
import com.elian.computeit.databinding.FragmentEditProfileBinding
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.*
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnSave
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnShowErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile)
{
	private val viewModel by viewModels<EditProfileViewModel>()
	private val binding by viewBinding(FragmentEditProfileBinding::bind)


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

		btnSave.setOnClickListener { viewModel.onAction(Save) }

		lifecycleScope.launch()
		{
			val info = viewModel.getProfileInfo()

			tietUsername.setText(info.username)
			tietBiography.setText(info.biography)
		}
	}

	private fun subscribeToEvents() = viewModel.apply2()
	{
		collectFlowWhenStarted(eventFlow)
		{
			when (it)
			{
				is OnSave             -> toast(R.string.message_info_successfully_updated)
				is OnShowErrorMessage -> toast(it.error.asString(context))
			}
		}
		collectLatestFlowWhenStarted(usernameState.map { it.error })
		{
			binding.tilUsername.error2 = getUsernameErrorMessage(context, it)
		}
	}
}