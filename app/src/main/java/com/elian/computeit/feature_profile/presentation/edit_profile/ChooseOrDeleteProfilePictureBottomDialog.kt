package com.elian.computeit.feature_profile.presentation.edit_profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.SimpleBottomSheetDialogFragment
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.BottomDialogChooseOrDeleteProfilePicBinding
import kotlinx.parcelize.Parcelize

class ChooseOrDeleteProfilePictureBottomDialog : SimpleBottomSheetDialogFragment<Nothing, ChooseOrDeleteProfilePictureBottomDialog.Event>(
	R.layout.bottom_dialog_choose_or_delete_profile_pic
) {
	private val binding by viewBinding(BottomDialogChooseOrDeleteProfilePicBinding::bind)

	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

		uri?.also { sendDialogEvent(Event.OnPictureSelected(it)) }

		dismiss()
	}
	private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionGranted ->

		if (!isPermissionGranted) return@registerForActivityResult

		getContent.launch("image/*")
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initializeUi()
	}


	private fun initializeUi() = using(binding) {
		btnSelectAPicture.setOnClickListener { requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
		btnDeleteCurrentPicture.setOnClickListener {
			sendDialogEvent(Event.OnDeleteImage)
			dismiss()
		}
	}

	sealed interface Event : Parcelable {
		@Parcelize
		class OnPictureSelected(val pictureUri: Uri) : Event

		@Parcelize
		object OnDeleteImage : Event
	}

	companion object {
		fun newInstance() = ChooseOrDeleteProfilePictureBottomDialog()
	}
}