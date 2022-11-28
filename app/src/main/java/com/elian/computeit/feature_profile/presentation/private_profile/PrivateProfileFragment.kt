package com.elian.computeit.feature_profile.presentation.private_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.extensions.navigateTo
import com.elian.computeit.core.presentation.util.extensions.text2
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentPrivateProfileBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        initUi()
    }


    private fun initUi() = binding.apply2()
    {
        lifecycleScope.launch()
        {
            viewModel.getProfileInfo().collect()
            {
                tvMainUsername.text2 = it.username
                tvUsername.text2 = it.username
                tvBiography.text2 = it.biography
                tvCreatedAt.text2 = getString(R.string.profile_account_created_at_ph).format(it.createdAt)
            }
        }

        btnEdit.setOnClickListener { navigate(R.id.action_profileFragment_to_editProfileFragment) }
        btnLogout.setOnClickListener()
        {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.alert_dialog_are_you_sure_you_want_to_log_out)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    lifecycleScope.launch()
                    {
                        viewModel.logout()
                        navigateTo<LoginActivity>()
                    }
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
                .show()
        }
    }
}