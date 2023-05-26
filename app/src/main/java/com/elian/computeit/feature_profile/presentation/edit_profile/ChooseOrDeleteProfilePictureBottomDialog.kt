package com.elian.computeit.feature_profile.presentation.edit_profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.showToast
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.BottomDialogChooseOrDeleteProfilePicBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.parcelize.Parcelize

class ChooseOrDeleteProfilePictureBottomDialog : BottomSheetDialogFragment(R.layout.bottom_dialog_choose_or_delete_profile_pic)
{
	private val binding by viewBinding(BottomDialogChooseOrDeleteProfilePicBinding::bind)

	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

		uri?.also { onEvent(Event.OnPictureSelected(it)) }

		dismiss()
	}
	private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionGranted ->

		if (!isPermissionGranted) return@registerForActivityResult

		getContent.launch("image/*")
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		initializeUi()
	}


	inline fun setOnEventListener(
		fragment: Fragment,
		crossinline onEvent: (event: Event) -> Unit,
	)
	{
		fragment.parentFragmentManager.setFragmentResultListener(this::class.qualifiedName!!, fragment.viewLifecycleOwner) { _, bundle ->

			onEvent(bundle.getParcelable("args")!!)
		}
	}


	private fun initializeUi() = using(binding)
	{
		btnSelectAPicture.setOnClickListener { requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
		btnDeleteCurrentPicture.setOnClickListener()
		{
			onEvent(Event.OnDeleteImage)
			dismiss()
		}
	}

	private fun onEvent(event: Event)
	{
		setFragmentResult(this::class.qualifiedName!!, bundleOf("args" to event))
	}


	sealed interface Event : Parcelable
	{
		@Parcelize
		class OnPictureSelected(val pictureUri: Uri) : Event

		@Parcelize
		object OnDeleteImage : Event
	}

	companion object
	{
		fun newInstance() = ChooseOrDeleteProfilePictureBottomDialog()
	}
}