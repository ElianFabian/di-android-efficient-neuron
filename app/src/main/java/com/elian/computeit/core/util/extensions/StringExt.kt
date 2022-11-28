package com.elian.computeit.core.util.extensions

val whitespacesBeforeScapeRegex = "[ ]+\n".toRegex()

fun String.trimWhitespacesBeforeNewLine() = replace(whitespacesBeforeScapeRegex, "\n")
