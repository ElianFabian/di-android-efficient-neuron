package com.elian.computeit.core.presentation.util.extensions

import android.widget.TextView
import androidx.core.view.isGone

var TextView.text2
    get() = text
    set(value)
    {
        isGone = value.isNullOrBlank()

        text = value
    }