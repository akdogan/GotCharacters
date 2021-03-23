package com.akdogan.swcharacters.datasource.remote

import com.akdogan.swcharacters.datasource.database.DatabaseGotCharacter

data class RemoteGotCharacter(
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val father: String,
    val mother: String,
    val spouse: String,
    val url: String
)

fun RemoteGotCharacter.toDatabaseModel(): DatabaseGotCharacter{
    return DatabaseGotCharacter(
        name = name,
        gender = gender,
        culture = culture,
        born = born,
        died = died,
        titles = titles,
        aliases = aliases,
        father = father,
        mother = mother,
        spouse = spouse,
        url = url
    )
}

//@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun getRemoteGotTestCharacter(
    name : String = "name",
    gender : String = "gender",
    culture : String ="culture",
    born : String = "born",
    died: String = "died",
    titles: List<String> = listOf("titles"),
    aliases : List<String> = listOf("aliases"),
    father : String= "father",
    mother: String = "mother",
    spouse: String = "spouse",
    url: String = "url"
): RemoteGotCharacter{
    return RemoteGotCharacter(
        name = name,
        gender = gender,
        culture = culture,
        born = born,
        died = died,
        titles = titles,
        aliases = aliases,
        father = father,
        mother = mother,
        spouse = spouse,
        url = url
    )
}