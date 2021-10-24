package com.example.project_brooklyn.item.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
	@PrimaryKey val uid : Int?,
	@ColumnInfo(name = "keyword") val keyword : String?
)