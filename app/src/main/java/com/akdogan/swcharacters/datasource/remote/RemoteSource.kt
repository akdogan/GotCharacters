package com.akdogan.swcharacters.datasource.remote

import android.util.Log

interface RemoteSource{

    suspend fun getCharacters(
        page: Int = 1,
        pageSize: Int = 50
    ): List<RemoteGotCharacter>


    suspend fun getSingleCharacter(url: String): RemoteGotCharacter?

    suspend fun getCharacterName(url: String): String

}



class DefaultRemoteSource(
    private val apiService: GotApiService = GotApi.retrofitService
) : RemoteSource{


    override suspend fun getCharacters(page: Int, pageSize: Int): List<RemoteGotCharacter> {
        return apiService.getCharacters(page, pageSize)
    }

    // Todo Write test to see how behaviour is if url parse fails
    override suspend fun getSingleCharacter(url: String): RemoteGotCharacter? {
        return getIdFromUrl(url)?.let { id ->
            apiService.getSingleCharacter(id).run {
                this?.copy(
                    url = cleanUrl(this.url),
                    father = getCharacterName(this.father),
                    mother = getCharacterName(this.mother),
                    spouse = getCharacterName(this.spouse)
                ).also {
                    Log.i("COPYTEST", "${it?.name}")
                }
            }
        }
    }

    private fun cleanUrl(url: String): String {
        return url.replace("www.", "")
    }

    override suspend fun getCharacterName(url: String): String {
        return getIdFromUrl(url)?.let{ id ->
            apiService.getSingleCharacter(id)?.let{
                it.name
            }
        } ?: ""
    }

    private fun getIdFromUrl(url: String): Int? {
        return url.substringAfterLast("/", "").toIntOrNull()
    }

}