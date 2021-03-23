package com.akdogan.swcharacters.datasource.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.akdogan.swcharacters.datasource.remote.RemoteGotCharacter
import com.akdogan.swcharacters.datasource.remote.getRemoteGotTestCharacter
import com.akdogan.swcharacters.datasource.remote.toDatabaseModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

class GotDatabaseTest {

    @RunWith(AndroidJUnit4::class)
    class SimpleEntityReadWriteTest {
        private lateinit var testDao: MyDao
        private lateinit var db: GotDatabase

        //@Before
        /*fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, GotDatabase::class.java
            ).build()
            testDao = db.databaseDao
        }*/

        @Before
        fun createDB2() {
            db = createMemoryDB()
            testDao = db.databaseDao
        }

        private fun createMemoryDB(): GotDatabase{
            val context = ApplicationProvider.getApplicationContext<Context>()
            return Room.inMemoryDatabaseBuilder(
                context, GotDatabase::class.java
            ).build()
        }

        private fun createDiskDB(): GotDatabase{
            val context = ApplicationProvider.getApplicationContext<Context>()
            return GotDatabase.getTestInstance(context)
        }

        @After
        @Throws(IOException::class)
        fun closeDb() {
            //runBlocking { db.clearAllTables() }
            db.close()
        }

        @Test
        fun writeUserAndDelete() {
            val testChar = getRemoteGotTestCharacter().toDatabaseModel()
            var result : DatabaseGotCharacter? = null
            runBlocking {
                testDao.insert(testChar)
                result = testDao.findByUrl(testChar.url)
            }
            assertEquals(testChar, result)
            runBlocking {
                testDao.delete(testChar)
                result = testDao.findByUrl(testChar.url)
            }
            assertEquals(null, result)
        }

        @Test
        fun writeUserAndUpdateManual(){
            val nameStringExpected = "nameStringExpected"
            val testChar = getRemoteGotTestCharacter().toDatabaseModel()
            val copyChar = testChar.copy(name = nameStringExpected)
            var resultName = ""
            runBlocking {
                testDao.insert(testChar)
                resultName = testDao.findByUrl(testChar.url)?.name ?: ""
            }
            assertEquals(testChar.name, resultName)
            runBlocking {
                testDao.delete(testChar)
                testDao.insert(copyChar)
                resultName = testDao.findByUrl(testChar.url)?.name ?: ""
            }
            assertEquals(nameStringExpected, resultName)
        }

        @Test
        @Throws(Exception::class)
        fun writeUserAndUpdate() {
            val urlID = "urlId"
            val fatherStringExpected = "fatherStringExpected"

            val testChar = getRemoteGotTestCharacter(
                url = urlID
            ).toDatabaseModel()
            val copyChar = testChar.copy(
                father = fatherStringExpected
            )
            var result: DatabaseGotCharacter? = null
            runBlocking {
                testDao.insert(testChar)
                testDao.update(copyChar)
                result = testDao.findByUrl(urlID)
            }
            assertEquals(result?.father, copyChar.father)

        }

    }
}