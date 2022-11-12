package com.elian.computeit.core.util.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast

inline fun <reified T : Activity> Activity.navigateTo()
{
    startActivity(Intent(this, T::class.java))
    finish()
}

fun Activity.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, text, duration).show()
}