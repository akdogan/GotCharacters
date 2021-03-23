package com.akdogan.swcharacters.datasource.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://anapioficeandfire.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GotApiService {
    @GET("characters/")
    suspend fun getCharacters(
        @Query("page")page: Int = 1,
        @Query("pageSize")pageSize: Int = 50
    ): List<RemoteGotCharacter>

    @GET("characters/{id}")
    suspend fun getSingleCharacter(@Path("id")id: Int): RemoteGotCharacter?

}

object GotApi {
    val retrofitService : GotApiService by lazy {
        retrofit.create(GotApiService::class.java) }
}