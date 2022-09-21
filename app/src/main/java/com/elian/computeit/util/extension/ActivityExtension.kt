package com.elian.computeit.util.extension

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes


fun Activity.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, text, duration).show()
}

fun Activity.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, resId, duration).show()
}
