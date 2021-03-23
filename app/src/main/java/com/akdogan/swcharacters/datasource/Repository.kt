package com.akdogan.swcharacters.datasource

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.akdogan.swcharacters.datasource.database.GotDatabase
import com.akdogan.swcharacters.datasource.database.MyDao
import com.akdogan.swcharacters.datasource.database.toDomainModel
import com.akdogan.swcharacters.datasource.remote.*


object ServiceLocator {
    private var REPOSITORY_INSTANCE: Repository? = null

    fun getDefaultRepository(
        app: Context,
    ): Repository {
        return REPOSITORY_INSTANCE ?: synchronized(this) {
            val database = GotDatabase.getInstance(app)
            MyRepository(
                database.databaseDao,
                DefaultRemoteSource()
            ).also {
                REPOSITORY_INSTANCE = it
            }
        }
    }

    /**
     * Call this function in tests to setup a TestRepository with the Fakes / Doubles required
     * The TestInstance will be saved as Instance and returned to all components asking for the
     * Default Instance
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setupTestRepository(
        app: Context,
        remoteSource: RemoteSource = DefaultRemoteSource(),
        dataBase: GotDatabase? = null
    ): Repository {
        return REPOSITORY_INSTANCE ?: synchronized(this) {
            var db = dataBase ?: GotDatabase.getTestInstance(app)
            MyRepository(
                db.databaseDao,
                remoteSource
            ).also {
                REPOSITORY_INSTANCE = it
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun cleanUpRepository(context: Context){
        GotDatabase.cleanUpTestInstance(context)
        REPOSITORY_INSTANCE = null
    }

}

interface Repository {
    val data: LiveData<List<DomainGotCharacter>>

    suspend fun fetchData()

    suspend fun getCharacter(url: String): DomainGotCharacter?

    fun getTestValue(): Int
}

class MyRepository(
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) // Could be removed later
    val database: MyDao,
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) // Could be removed later
    val remoteSource: RemoteSource
) : Repository {

    private var test = 0


    override val data = Transformations.map(database.getAll()) { databaseList ->
        databaseList.map {
            it.toDomainModel()
        }
    }

    init {
        test = (1111..9999).random()
    }

    override fun getTestValue(): Int {
        return test
    }

    override suspend fun fetchData() {
        fetchDataAndCache()
    }

    suspend fun fetchDataAndCache() {
        var pageIndex = 1
        var hasContent = true
        while (hasContent) {
            /*var dataPackage : List<DatabaseGotCharacter> = emptyList()
                withContext(Dispatchers.IO) {
                    dataPackage = remoteSource.getCharacters(pageIndex).filter {
                        !it.name.isNullOrBlank()
                    }.map {
                        it.toDatabaseModel()
                    }
                }
            if (dataPackage.isEmpty()){
                hasContent = false
            } else {
                database.insertAll(dataPackage)
            }*/
            remoteSource.getCharacters(pageIndex).filter {
                !it.name.isNullOrBlank()
            }.map {
                it.toDatabaseModel()
            }.apply {
                if (this.isEmpty()) {
                    hasContent = false
                } else {
                    database.insertAll(this)
                }
            }
            pageIndex++
            Log.i("API_LOAD_ALL", "fetchdata while loop with pIndex: $pageIndex")
        }

    }

    override suspend fun getCharacter(url: String): DomainGotCharacter? {
        return getCharacterUpdateRemote(url)
    }

    suspend fun getCharacterUpdateRemote(url: String): DomainGotCharacter? {
        // Try to update the entry to get actual names for references
        try {
            val remoteItem = remoteSource.getSingleCharacter(url)
            println("REPO PROD CODE: remote item result = $remoteItem")
            remoteItem?.let {
                database.update(it.toDatabaseModel())
            }
        } catch (e: Exception) {
            Log.i("COPYTEST", "catchBlock called with $e")
        } finally {
            return try {
                database.findByUrl(url)?.toDomainModel()
            } catch (e: Exception) {
                // TODO Error handling
                null
            }
        }
    }
}