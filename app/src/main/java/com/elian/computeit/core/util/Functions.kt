package com.elian.computeit.core.util

fun getDivisiblePairsInRange(range: IntRange): List<Pair<Int, Int>>
{
    val divisiblePairs = mutableListOf<Pair<Int, Int>>()

    val reversedRange = range.reversed()

    for (a in reversedRange)
    {
        for (b in reversedRange)
        {
            if (a == b || a % b != 0) continue

            divisiblePairs.add(a to b)
        }
    }

    return divisiblePairs.reversed()
}