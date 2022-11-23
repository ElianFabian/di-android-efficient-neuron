package com.elian.computeit.core.presentation.util.extensions

import android.widget.TextView
import androidx.core.view.isGone

var TextView.text2: CharSequence
    get() = text
    set(value)
    {
        text = value

        isGone = text.isNullOrBlank()
    }