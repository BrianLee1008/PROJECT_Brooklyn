package com.example.project_brooklyn.item.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_brooklyn.item.room.entity.HistoryEntity

@Dao
interface HistoryDao {

	@Query("SELECT * FROM history")
	fun getAll() : LiveData<List<HistoryEntity>>

	@Insert
	fun insertKeyword(history : HistoryEntity)

//	@Query("DELETE FROM history WHERE keyword = :keyword")
//	fun deleteHistory(keyword: HistoryEntity)
}