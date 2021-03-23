package com.akdogan.swcharacters.datasource.remote

import androidx.annotation.VisibleForTesting

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
class RemoteSourceFake : RemoteSource{

    private val internalIdToStringSet: HashMap<String, String> = hashMapOf()

    private val internalDataSet: MutableList<RemoteGotCharacter> = mutableListOf()

    fun addCharacter(item: RemoteGotCharacter){
        internalDataSet.add(item)
    }

    fun addInternalIdReference(urlPartWithId: String, resultName: String){
        internalIdToStringSet[urlPartWithId] = resultName
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
            internalDataSet.find { it.url == url }.run{
                println("Remote Source Fake Result of internaldataset.find: $this")
                this?.copy(
                    father = getCharacterName(this.father),
                    mother = getCharacterName(this.mother),
                    spouse = getCharacterName(this.spouse)
                )
            }
        }
    }

    override suspend fun getCharacterName(url: String): String {
        val result = internalIdToStringSet[url] ?: ""
        println("REMOTE SOURCE FAKE getCharacterName with url $url and result $result")
        return result
    }

    private fun getIdFromUrl(url: String): Int? {
        return url.substringAfterLast("/", "").toIntOrNull()
    }

}