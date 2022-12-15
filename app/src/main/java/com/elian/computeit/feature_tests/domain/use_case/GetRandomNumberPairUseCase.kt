package com.elian.computeit.feature_tests.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.util.constants.receiveArgs
import com.elian.computeit.core.util.getDivisiblePairsInRange
import com.elian.computeit.feature_tests.domain.args.TestArgs
import javax.inject.Inject
import kotlin.random.Random

class GetRandomNumberPair @Inject constructor(
	savedState: SavedStateHandle,
)
{
	private val args = savedState.receiveArgs<TestArgs>()!!

	private val _range = args.range.let { it.min..it.max }
	private val _divisiblePairs: List<NumberPair> = if (args.operation == Operation.Division)
	{
		getDivisiblePairsInRange(
			start = _range.first,
			end = _range.last,
			ignoreSelfDivision = true,
		).map { NumberPair(it.first, it.second) }
	}
	else emptyList()


	/**
	 * Returns a random number pair.
	 * @param oldPair if isn't null the returned pair will always be different from the old one.
	 */
	operator fun invoke(oldPair: NumberPair? = null): NumberPair
	{
		if (oldPair == null) return getRandomPairByOperation()

		while (true)
		{
			val newPair = getRandomPairByOperation()

			val isDifferent = newPair.first != oldPair.first || newPair.second != oldPair.second
			val isDifferentInReverse = newPair.first != oldPair.second || newPair.second != oldPair.first

			if (isDifferent && isDifferentInReverse) return newPair
		}
	}

	private fun getRandomPairByOperation(): NumberPair
	{
		if (args.operation == Operation.Division) return _divisiblePairs.random(randomSeed)

		if (args.operation == Operation.Subtraction) while (true)
		{
			val newPair = getRandomNumberPair(_range)

			if (newPair.first != newPair.second) return newPair
		}

		return getRandomNumberPair(_range)
	}
}


private val randomSeed = Random(System.currentTimeMillis())

private fun getRandomNumberPair(range: IntRange): NumberPair
{
	val first = range.random(randomSeed)
	val second = range.random(randomSeed)

	return NumberPair(first, second)
}