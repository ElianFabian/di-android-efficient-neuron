package com.elian.computeit.core.util.constants

import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle

private const val EXTRA_ARGS = "EXTRA_ARGS"

interface Args : Parcelable

fun Args.toBundle() = bundleOf(EXTRA_ARGS to this)

fun <T : Args> Fragment.arguments() = lazy { arguments?.getParcelable<T>(EXTRA_ARGS)!! }
fun <T : Args> SavedStateHandle.arguments() = get<T>(EXTRA_ARGS)