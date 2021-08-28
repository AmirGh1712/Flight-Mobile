package com.example.flightmobileapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ServerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveServer(server:ServersEntity)
    @Query("select * from ServersEntity")
    fun readServer():List<ServersEntity>
    @Delete
    fun deleteData(server: ServersEntity)
    @Update
    fun updateData(server:ServersEntity)
    @Query("select * from ServersEntity ORDER BY time DESC LIMIT:k")
    fun lastFiveConnections(k:Int):List<ServersEntity>
}