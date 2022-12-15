package com.elian.computeit.feature_tests.domain.args

import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.util.constants.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestArgs(
	val operation: Operation,
	val range: Range,
	val totalTimeInSeconds: Int,
) : Args