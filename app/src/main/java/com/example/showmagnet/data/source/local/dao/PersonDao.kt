package com.example.showmagnet.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.showmagnet.data.source.local.model.PersonDb
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(data: List<PersonDb>)

    @Query("SELECT * FROM person_table where type = :type and timeWindow = :timeWindow")
    fun getPeople(type: String, timeWindow: String): Flow<List<PersonDb>>

    @Query("DELETE FROM person_table where type = :type and timeWindow = :timeWindow")
    suspend fun deleteAllPeople(type: String, timeWindow: String)
}