package com.elian.computeit.feature_tests.domain.args

import com.elian.computeit.core.domain.models.TestInfo
import com.elian.computeit.core.util.constants.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestDetailsArgs(
	val testInfo: TestInfo,
) : Args