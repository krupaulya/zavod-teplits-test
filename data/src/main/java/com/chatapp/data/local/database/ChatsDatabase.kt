package com.chatapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chatapp.data.local.database.dao.ChatsDao
import com.chatapp.data.local.database.dao.CountryDao
import com.chatapp.data.local.database.entity.ChatsData
import com.chatapp.data.local.database.entity.CountryData
import com.chatapp.data.local.database.entity.UserData

@Database(entities = [CountryData::class, ChatsData::class, UserData::class], version = 5, exportSchema = false)
abstract class ChatsDatabase: RoomDatabase() {

    abstract fun countryDao(): CountryDao
    abstract fun chatsDao(): ChatsDao
}