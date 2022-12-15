package com.elian.computeit.feature_tests.domain.args

import com.elian.computeit.core.util.constants.Args
import com.elian.computeit.feature_tests.domain.model.TestInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestDetailsArgs(
	val sender: Sender,
	val testInfo: TestInfo,
) : Args
{
	enum class Sender
	{
		Test,
		Home,
	}
}