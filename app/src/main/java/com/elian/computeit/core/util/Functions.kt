package com.elian.computeit.core.util

fun getDivisiblePairsInRange(start: Int, end: Int): List<Pair<Int, Int>>
{
	if (start == 0) throw IllegalArgumentException("start parameter can't be 0")
	if (end > start) throw IllegalArgumentException("end ($end) can't be greater than start ($start)")

	val divisiblePairs = mutableListOf<Pair<Int, Int>>()

	val reversedRange = end downTo start

	for (a in reversedRange)
	{
		for (b in (a - 1) downTo start)
		{
			if (a % b != 0) continue

			divisiblePairs += a to b
		}
	}

	return divisiblePairs.reversed()
}