package com.elian.computeit.core.util.extensions

fun Any.format(format: String) = String.format(format, this)

fun <T> T.apply2(block: T.() -> Unit)
{
	apply(block)
}