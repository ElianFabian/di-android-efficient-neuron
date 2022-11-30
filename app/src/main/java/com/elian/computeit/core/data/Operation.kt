package com.elian.computeit.core.data

enum class Operation(
	val symbol: String,
	val calculate: (a: Int, b: Int) -> Int,
)
{
	Addition(
		symbol = "+",
		calculate = { a, b -> a + b },
	),
	Subtraction(
		symbol = "−",
		calculate = { a, b -> a - b },
	),
	Multiplication(
		symbol = "×",
		calculate = { a, b -> a * b },
	),
	Division(
		symbol = "÷",
		calculate = { a, b -> a / b },
	);

	companion object
	{
		fun fromSymbol(symbol: String) = symbolToOperation[symbol]!!

		private val symbolToOperation = values().associateBy { it.symbol }
		val nameToSymbol = values().associate { it.name to it.symbol }
		val symbolToName = values().associate { it.symbol to it.name }
		val symbols = values().map { it.symbol }
	}

	operator fun invoke(firstNumber: Int, secondNumber: Int) = calculate(firstNumber, secondNumber)
}