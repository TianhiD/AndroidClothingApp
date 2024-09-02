package com.example.eksamen_store_app.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eksamen_store_app.data.History

@Dao
interface  HistoryDao {
    @Query("SELECT * FROM OrderHistory")
    suspend fun getAllHistoryItems(): List<History>

    @Delete
    suspend fun deleteHistoryItem(historyItem: History)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(history: List<History>)

}