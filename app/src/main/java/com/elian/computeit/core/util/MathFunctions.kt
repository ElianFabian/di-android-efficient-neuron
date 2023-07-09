package com.elian.computeit.core.util

// https://stackoverflow.com/questions/75062834/how-to-get-all-the-divisible-pairs-in-a-range-faster/
fun getAllDivisiblePairsInRange(
	start: Int,
	end: Int,
	ignoreSelfDivision: Boolean = false,
): Array<IntArray> {
	require(start != 0) { "start parameter can't be $start" }

	val divisiblePairsCount = getAllDivisiblePairsInRangeCount(start, end, ignoreSelfDivision)

	val divisiblePairs = Array(divisiblePairsCount) { intArrayOf(0, 0) }

	var ratio = end / start
	var denominator = start
	var pairIndex = 0

	while (ratio >= 1) {
		for (multiple in 1..ratio) {
			val numerator = denominator * multiple

			if (ignoreSelfDivision && denominator == numerator) continue

			divisiblePairs[pairIndex][0] = numerator
			divisiblePairs[pairIndex][1] = denominator

			pairIndex++
		}

		ratio = end / ++denominator
	}

	return divisiblePairs
}

fun getAllDivisiblePairsInRangeCount(
	start: Int,
	end: Int,
	ignoreSelfDivision: Boolean = false,
): Int {
	require(start != 0) { "start parameter can't be $start" }

	var ratio = end / start
	var denominator = start

	var count = 0

	while (ratio >= 1) {
		for (multiple in 1..ratio) {
			val numerator = denominator * multiple

			if (ignoreSelfDivision && denominator == numerator) continue

			count++
		}

		ratio = end / ++denominator
	}

	return count
}