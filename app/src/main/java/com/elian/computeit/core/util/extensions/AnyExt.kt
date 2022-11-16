package com.elian.computeit.core.util.extensions

fun <T> T.apply2(block: T.() -> Unit)
{
    apply(block)
}