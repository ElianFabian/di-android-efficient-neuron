package com.elian.computeit.core.util.extensions

import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

fun TextView.onTextChangedClearError()
{
    addTextChangedListener { error = null }
}

fun TextView.onTextChangedClearError2To(textInputLayout: TextInputLayout)
{
    addTextChangedListener { textInputLayout.error2 = null }
}