package com.elian.computeit.core.util

import android.content.Context
import androidx.annotation.StringRes
import com.elian.computeit.R

sealed class UiText
{
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any?,
    ) : UiText()

    fun asString(context: Context?) = when (this)
    {
        is DynamicString  -> this.value
        is StringResource -> context?.getString(this.resId, *args) ?: throw NullPointerException()
    }

    companion object
    {
        fun unknownError() = StringResource(R.string.error_unknown)
    }
}