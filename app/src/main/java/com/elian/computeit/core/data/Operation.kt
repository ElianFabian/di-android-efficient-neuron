package com.elian.computeit.core.data

enum class Operation(
	val symbol: String,
	val calculate: (a: Int, b: Int) -> Int,
)
{
	SUM(
		symbol = "+",
		calculate = { a, b -> a + b },
	),
	SUBTRACT(
		symbol = "−",
		calculate = { a, b -> a - b },
	),
	MULTIPLY(
		symbol = "×",
		calculate = { a, b -> a * b },
	),
	DIVIDE(
		symbol = "÷",
		calculate = { a, b -> a / b },
	);

	companion object
	{
		fun from(symbol: String) = symbolToOperation[symbol]!!

		private val symbolToOperation = values().associateBy { it.symbol }
	}

	operator fun invoke(firstNumber: Int, secondNumber: Int) = calculate(firstNumber, secondNumber)
}