package com.elian.computeit.core.presentation.util.extensions

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.navigateTo()
{
    startActivity(Intent(this, T::class.java))
    finish()
}