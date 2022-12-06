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
		fun fromName(name: String) = nameToOperation[name]!!

		private val symbolToOperation = values().associateBy { it.symbol }
		private val nameToOperation = values().associateBy { it.name }
		val nameToSymbol = values().associate { it.name to it.symbol }
	}

	operator fun invoke(firstNumber: Int, secondNumber: Int) = calculate(firstNumber, secondNumber)
}