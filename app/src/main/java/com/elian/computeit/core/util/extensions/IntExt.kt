package com.elian.computeit.core.util.extensions

import kotlin.math.pow

fun Int.digitCount(): Int
{
    if (this == 0) return 1

    var count = 0
    var currentNumber = this

    while (currentNumber > 0)
    {
        currentNumber /= 10
        count++
    }

    return count
}

fun Int.clampLength(maxLength: Int): Int
{
    val numberLength = this.digitCount()
    val charactersToDropLast = numberLength - maxLength

    return when
    {
        charactersToDropLast > 0 -> numberLength / 10.toDouble().pow(charactersToDropLast).toInt()
        else                     -> this
    }
}