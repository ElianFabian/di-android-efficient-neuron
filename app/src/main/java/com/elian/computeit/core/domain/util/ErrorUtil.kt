package com.elian.computeit.core.domain.util

import com.elian.computeit.core.util.Error

/**
 * Returns true if any error is different from null.
 */
fun checkIfError(vararg args: Error?) = args.any { it != null }