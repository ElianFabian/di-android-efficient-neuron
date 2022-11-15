package com.elian.computeit.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NumberPair(
    val first: Int = 0,
    val second: Int = 0,
) : Parcelable
