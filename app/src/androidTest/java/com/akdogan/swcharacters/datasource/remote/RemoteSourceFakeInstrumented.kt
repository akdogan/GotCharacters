package com.akdogan.swcharacters.datasource.remote

import androidx.annotation.VisibleForTesting

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
class RemoteSourceFakeInstrumented : RemoteSource{

    private val internalDataSet: MutableList<RemoteGotCharacter> = mutableListOf()

    fun addCharacter(item: RemoteGotCharacter){
        internalDataSet.add(item)
    }


    override suspend fun getCharacters(page: Int, pageSize: Int): List<RemoteGotCharacter> {
        return if (page == 1){
            internalDataSet
        } else {
            emptyList()
        }
    }

    override suspend fun getSingleCharacter(url: String): RemoteGotCharacter? {
        println("RemoteSourceFake internal dataset: $internalDataSet")
        /*var result = internalDataSet.find{ it.url == url}
        println("RemoteSourceFake result value: $result" )
        return result
        */
        return getIdFromUrl(url)?.let{
            internalDataSet.find { it.url == url }
        }
    }

    override suspend fun getCharacterName(url: String): String {
        return ""
    }

    private fun getIdFromUrl(url: String): Int? {
        return url.substringAfterLast("/", "").toIntOrNull()
    }

}