package com.elian.computeit.core.util.extensions

fun Int.clampLength(maxLength: Int): Int
{
    val numberToString = this.toString()
    val charactersToDropLast = numberToString.length - maxLength

    return when
    {
        charactersToDropLast > 0 -> numberToString.dropLast(charactersToDropLast).toInt()
        else                     -> this
    }
}