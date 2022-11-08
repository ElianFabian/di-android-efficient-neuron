package com.elian.computeit.core.util.extensions

import kotlin.math.pow

fun Long.digitCount(): Int
{
    if (this == 0L) return 1

    var count = 0
    var currentNumber = this

    while (currentNumber > 0)
    {
        currentNumber /= 10
        count++
    }

    return count
}

fun Long.clampLength(maxLength: Int): Long
{
    val numberLength = this.digitCount()
    val charactersToDropLast = numberLength - maxLength

    return when
    {
        charactersToDropLast > 0 -> this / 10.0.pow(charactersToDropLast).toInt()
        else                     -> this
    }
}

fun Int.clampLength(maxLength: Int) = this.toLong().clampLength(maxLength).toInt()


fun Long.append(number: Long): Long
{
    val power = number.digitCount()

    return (this * 10.0.pow(power) + number).toLong()
}

fun Int.append(number: Int) = this.toLong().append(number.toLong()).toInt()

fun Long.fromMillisToSeconds() = this / 1000f

fun Float.ifNaNReturnZero() = if (isNaN()) 0F else this