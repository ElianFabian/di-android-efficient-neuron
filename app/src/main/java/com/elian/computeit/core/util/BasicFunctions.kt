package com.elian.computeit.core.util

inline fun <T> using(receiver: T, block: T.() -> Unit) = with(receiver, block)