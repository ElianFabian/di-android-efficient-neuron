package com.elian.computeit.core.presentation.util

import android.content.Context
import com.elian.computeit.core.util.UiText

// From: https://github.com/philipplackner/SocialNetworkTwitch/blob/development/app/src/main/java/com/plcoding/socialnetworktwitch/core/presentation/util/UiTextUtil.kt
fun UiText.asString(context: Context): String
{
    return when (this)
    {
        is UiText.DynamicString  -> this.value
        is UiText.StringResource -> context.getString(this.resId)
    }
}