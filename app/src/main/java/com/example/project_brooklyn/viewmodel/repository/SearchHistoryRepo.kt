package com.example.project_brooklyn.viewmodel.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.project_brooklyn.item.room.AppDatabase
import com.example.project_brooklyn.item.room.entity.HistoryEntity


class SearchHistoryRepo(application: Application) {

	companion object {
		const val APP_DATABASE = "AppDatabase"
	}

	// AppDatabase Build
	val db = Room.databaseBuilder(
		application, AppDatabase::class.java, APP_DATABASE
	).fallbackToDestructiveMigration()
		.build()

	// Dao Instance
	val historyDao = db.historyDao()


	// History CRUD

	fun getAllHistory(): LiveData<List<HistoryEntity>> =
		historyDao.getAll()

	fun historyInsert(history: HistoryEntity) {
		historyDao.insertKeyword(history)
	}

//	fun historyDelete(history: HistoryEntity){
//		historyDao.deleteHistory(history)
//	}


}