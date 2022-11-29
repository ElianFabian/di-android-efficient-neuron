package com.elian.computeit.feature_tips.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tip_table")
data class Tip(
	val title: String,
	val example: String,

	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
) :
	Serializable