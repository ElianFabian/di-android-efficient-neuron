package com.elian.computeit.core.presentation.util.extensions

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.show(manager: FragmentManager) {
	if (dialog?.isShowing == true) return
	
	show(manager, "")
}