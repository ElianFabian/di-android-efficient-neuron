package com.elian.computeit.feature_profile.presentation.private_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.extensions.text2
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentPrivateProfileBinding
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class PrivateProfileFragment : Fragment()
{
    private val viewModel by viewModels<PrivateProfileViewModel>()
    private lateinit var binding: FragmentPrivateProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentPrivateProfileBinding.inflate(inflater)
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
        btnEdit.setOnClickListener { navigate(R.id.action_profileFragment_to_editProfileFragment) }
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
            tvMainUsername.text2 = username
            tvUsername.text2 = username
            tvCreatedAt.text2 = getString(R.string.profile_account_created_at_ph).format(createdAt)
        }
    }
}