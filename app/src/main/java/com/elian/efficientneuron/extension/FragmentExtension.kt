package com.elian.efficientneuron.extension

import android.app.Activity
import android.widget.Toast


fun Activity.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, text, duration).show()
}
