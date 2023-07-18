package com.example.showmagnet.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.showmagnet.data.source.local.model.PersonDb

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(data: List<PersonDb>)

    @Query("SELECT * FROM person_table where type = :type")
    fun getAllPeople(type: String): List<PersonDb>

    @Query("DELETE FROM person_table where type = :type")
    suspend fun deleteAllPeople(type: String)
}