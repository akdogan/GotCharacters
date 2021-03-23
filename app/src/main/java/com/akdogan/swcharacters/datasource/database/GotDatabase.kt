package com.akdogan.swcharacters.datasource.database

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class GotConverters{
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun listToString(value: List<String>): String{
        /*val builder = StringBuilder()
        value.forEach {
            builder.append("$it;;")
        }
        return builder.toString()*/
        val adapter = moshi.adapter<List<String>>(List::class.java)
        return adapter.toJson(value)
    }
    @TypeConverter
    fun stringToList(value: String): List<String>{
        /*val resultList = mutableListOf<String>()
        return value.split(";;", ignoreCase = true)*/
        val adapter = moshi.adapter<List<String>>(List::class.java)
        return adapter.fromJson(value) ?: emptyList()
    }
}

@Database(entities = [DatabaseGotCharacter::class], version = 2, exportSchema = false)
@TypeConverters(GotConverters::class)
abstract class GotDatabase : RoomDatabase() {

    abstract val databaseDao: MyDao

    companion object {
        private const val INSTANCE_NAME = "got_database"
        private const val TEST_INSTANCE_NAME = "got_database_test"

        @Volatile
        private var INSTANCE: GotDatabase? = null

        fun getInstanceOld(context: Context): GotDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        GotDatabase::class.java,
                        INSTANCE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun getInstance(context: Context): GotDatabase{
            return synchronized(this){
                INSTANCE ?: run {
                    createInstance(context, INSTANCE_NAME).apply {
                        INSTANCE = this
                    }
                }
            }
        }

        private fun createInstance(context: Context, instanceType: String): GotDatabase{
            return Room.databaseBuilder(
                context,
                GotDatabase::class.java,
                instanceType
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @VisibleForTesting(otherwise = VisibleForTesting.NONE)
        fun getTestInstance(context: Context): GotDatabase{
            return synchronized(this){
                INSTANCE ?: run{
                    createInstance(context, TEST_INSTANCE_NAME).apply {
                        INSTANCE = this
                    }
                }
            }
        }

        @VisibleForTesting(otherwise = VisibleForTesting.NONE)
        fun cleanUpTestInstance(context: Context){
            context.deleteDatabase(TEST_INSTANCE_NAME);
        }


    }
}