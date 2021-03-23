package com.akdogan.swcharacters.datasource

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.akdogan.swcharacters.datasource.database.DatabaseGotCharacter
import com.akdogan.swcharacters.datasource.database.GotDatabase
import com.akdogan.swcharacters.datasource.remote.toDatabaseModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException

@Config(manifest=Config.NONE)
@RunWith(AndroidJUnit4::class)
class MyRepositoryTest {
    private lateinit var repo: MyRepository
    private lateinit var db: GotDatabase

    @Before
    fun setupRepositoryAndDataBase() {
        val remoteSourceFake = setupRemoteSourceFakeWithTestData()
        db = createMemoryDB()
        repo = MyRepository(
            db.databaseDao,
            remoteSourceFake
        )
    }

    private fun createMemoryDB(): GotDatabase{
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(
            context, GotDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        //runBlocking { db.clearAllTables() }
        db.close()
    }

    @Test
    fun testDataBase_insertAndRetrieve(){
        var result: DatabaseGotCharacter? = null
        runBlocking {
            with (db.databaseDao){
                insert(testChar.toDatabaseModel())
                result = findByUrl(testChar.url)
            }
        }
        assertEquals(testChar.toDatabaseModel(), result)
    }

    @Test
    fun testAdd() {
        runBlocking {
            println("Remote DataSource setup with: ${repo.remoteSource.getCharacters(1, 0)}")
            println("Remote DataSource get single char directly with ${testChar.url}: ${
                repo.remoteSource.getSingleCharacter(testChar.url)
            }")
            repo.fetchDataAndCache()
            val resultChar = repo.getCharacter(testChar.url)
            println("Repo ResultChar: $resultChar")
            val dbDirectChar = repo.database.findByUrl(testChar.url)
            println("DB ResultChar: $dbDirectChar")
            assertEquals(fatherName, resultChar?.father)
            assertEquals(motherName, resultChar?.mother)
            assertEquals(spouseName, resultChar?.spouse)
        }
    }
}