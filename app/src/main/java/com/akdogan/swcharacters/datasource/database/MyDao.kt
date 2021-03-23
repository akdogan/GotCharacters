package com.akdogan.swcharacters.datasource.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyDao {
    @Query("SELECT * FROM got_characters_table")
    fun getAll(): LiveData<List<DatabaseGotCharacter>>

    @Query("SELECT * FROM got_characters_table WHERE url = :url")
    suspend fun findByUrl(url: String): DatabaseGotCharacter?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<DatabaseGotCharacter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(char: DatabaseGotCharacter)

    @Query("DELETE from got_characters_table")
    suspend fun deleteAll()

    @Update//(entity = DatabaseGotCharacter::class)
    suspend fun update(char: DatabaseGotCharacter)

    @Delete
    suspend fun delete(char: DatabaseGotCharacter)
}
