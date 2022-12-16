package com.elian.computeit.feature_tests.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.util.constants.TestArgKeys
import com.elian.computeit.core.util.getDivisiblePairsInRange
import javax.inject.Inject
import kotlin.random.Random

class GetRandomNumberPairUseCase @Inject constructor(
	savedState: SavedStateHandle,
)
{
	private val _rangeBounds = savedState.get<Range>(TestArgKeys.OperationRange)!!
	private val _operation = savedState.get<Operation>(TestArgKeys.OperationType)!!

	private val _range = _rangeBounds.min.._rangeBounds.max
	private val _divisiblePairs: List<NumberPair> = if (_operation == Operation.Division)
	{
		getDivisiblePairsInRange(
			start = _rangeBounds.min,
			end = _rangeBounds.max,
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
		if (_operation == Operation.Division) return _divisiblePairs.random(randomSeed)

		if (_operation == Operation.Subtraction) while (true)
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