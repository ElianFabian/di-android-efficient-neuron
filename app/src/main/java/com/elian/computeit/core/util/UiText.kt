package com.elian.computeit.core.util

import android.content.Context
import androidx.annotation.StringRes
import com.elian.computeit.R

// From: https://github.com/philipplackner/SocialNetworkTwitch/blob/development/app/src/main/java/com/plcoding/socialnetworktwitch/core/util/UiText.kt
sealed class UiText
{
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any = emptyArray(),
    ) : UiText()

    companion object
    {
        fun unknownError(): UiText
        {
            return UiText.StringResource(R.string.error_unknown)
        }
    }
}