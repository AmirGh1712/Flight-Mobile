package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ServersEntity {
    @PrimaryKey
    var URL: String =""
    @ColumnInfo
    var time: Long= System.currentTimeMillis()

}