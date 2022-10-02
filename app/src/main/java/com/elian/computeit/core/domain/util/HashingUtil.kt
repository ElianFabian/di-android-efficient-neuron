package com.elian.computeit.core.domain.util

import com.squareup.okhttp.internal.Util

object HashingUtil
{
    fun hash(text: String) = Util.shaBase64(text)
}