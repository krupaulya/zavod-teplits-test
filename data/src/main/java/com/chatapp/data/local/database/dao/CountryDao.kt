package com.chatapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chatapp.data.local.database.entity.CountryData

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): List<CountryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryData>)
}