package com.elian.computeit.feature_profile.presentation.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentEditProfileBinding
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class EditProfileFragment : Fragment()
{
    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var binding: FragmentEditProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        subscribeToEvents()
        initUi()
    }


    private fun initUi() = binding.apply2()
    {
        btnSave.setOnClickListener { TODO() }
    }

    private fun subscribeToEvents() = viewModel.apply2()
    {
        collectLatestFlowWhenStarted(infoState.filterNotNull())
        {
            initViews(it)
        }
    }

    private fun initViews(info: ProfileInfo) = info.apply2()
    {
        binding.apply()
        {
            tilUsername.editText!!.setText(username)
        }
    }
}