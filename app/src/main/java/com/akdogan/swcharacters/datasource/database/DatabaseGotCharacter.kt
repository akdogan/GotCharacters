package com.akdogan.swcharacters.datasource.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akdogan.swcharacters.datasource.DomainGotCharacter
import com.akdogan.swcharacters.datasource.remote.RemoteGotCharacter

@Entity(tableName = "got_characters_table")
data class DatabaseGotCharacter(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "gender")val gender: String,
    @ColumnInfo(name = "culture")val culture: String,
    @ColumnInfo(name = "born")val born: String,
    @ColumnInfo(name = "died")val died: String,
    @ColumnInfo(name = "titles")val titles: List<String>,
    @ColumnInfo(name = "aliases")val aliases: List<String>,
    @ColumnInfo(name = "father")val father: String,
    @ColumnInfo(name = "mother")val mother: String,
    @ColumnInfo(name = "spouse")val spouse: String,
    @PrimaryKey(autoGenerate = false)val url: String
)

fun DatabaseGotCharacter.toDomainModel(): DomainGotCharacter {
    return DomainGotCharacter(
        //id = id,
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