package com.elian.computeit.core.util

fun getDivisiblePairsInRange(minValue: Int, maxValue: Int): List<Pair<Int, Int>>
{
	val divisiblePairs = mutableListOf<Pair<Int, Int>>()

	val reversedRange = maxValue downTo minValue

	for (a in reversedRange)
	{
		for (b in a downTo minValue)
		{
			if (b == 0 || a % b != 0 || a == b) continue

			divisiblePairs += a to b
		}
	}

	return divisiblePairs.reversed()
}