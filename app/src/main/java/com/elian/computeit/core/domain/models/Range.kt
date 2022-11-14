package com.elian.computeit.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Range(val min: Int, val max: Int) : Parcelable