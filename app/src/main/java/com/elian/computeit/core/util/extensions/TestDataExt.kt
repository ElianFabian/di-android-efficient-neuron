package com.elian.computeit.core.util.extensions

import com.elian.computeit.feature_tests.data.models.TestData

val TestData.isError get() = expectedResult != insertedResult