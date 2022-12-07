package com.elian.computeit.core.presentation.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

inline fun <reified T : Activity> Activity.navigateTo(
	args: Bundle = Bundle(),
	finish: Boolean = true,
)
{
	startActivity(Intent(this, T::class.java).putExtras(args))
	if (finish) finish()
}

fun Activity.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
	Toast.makeText(this, text, duration).show()
}