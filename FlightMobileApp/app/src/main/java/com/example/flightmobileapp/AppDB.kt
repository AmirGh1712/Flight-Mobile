package com.example.flightmobileapp

import androidx.room.Database
import androidx.room.RoomDatabase
@Database (entities = [ServersEntity::class],version = 1)
abstract class AppDB:RoomDatabase() {
    abstract fun serverDAO():ServerDAO

}