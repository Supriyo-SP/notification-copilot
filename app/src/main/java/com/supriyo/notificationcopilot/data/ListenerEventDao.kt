package com.supriyo.notificationcopilot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ListenerEventDao {
    @Insert
    suspend fun insert(event: ListenerEventEntity)

    @Query("SELECT * FROM listener_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<ListenerEventEntity>>
}
