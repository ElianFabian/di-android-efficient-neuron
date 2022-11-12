package com.elian.computeit.core.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

inline fun <reified T : Activity> Activity.navigateTo(args: Bundle = Bundle())
{
    startActivity(Intent(this, T::class.java).putExtras(args))
    finish()
}

fun Activity.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, text, duration).show()
}