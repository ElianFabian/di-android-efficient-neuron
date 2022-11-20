package com.elian.computeit.core.presentation.util.extensions

import android.widget.RadioButton
import android.widget.RadioGroup

val RadioGroup.checkedRadioButton: RadioButton? get() = findViewById(checkedRadioButtonId)