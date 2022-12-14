package com.elian.computeit.feature_profile.presentation.edit_profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.BottomDialogChooseOrDeleteProfilePicBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseOrDeleteProfilePictureBottomDialog(
	private val onPictureSelected: (Uri?) -> Unit,
	private val onDeleteImage: () -> Unit,
) : BottomSheetDialogFragment(R.layout.bottom_dialog_choose_or_delete_profile_pic)
{
	private val binding by viewBinding(BottomDialogChooseOrDeleteProfilePicBinding::bind)

	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent())
	{
		onPictureSelected(it)

		dismiss()
	}
	private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission())
	{
		if (!it) return@registerForActivityResult

		getContent.launch("image/*")
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		initUi()
	}


	private fun initUi() = using(binding)
	{
		btnSelectAPicture.setOnClickListener { requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
		btnDeleteCurrentPicture.setOnClickListener()
		{
			onDeleteImage()
			dismiss()
		}
	}
}