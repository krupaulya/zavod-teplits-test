package com.chatapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryData(
    val name: String,
    @PrimaryKey val code: String,
    val iso: String,
    val flag: String,
    val mask: String
)
