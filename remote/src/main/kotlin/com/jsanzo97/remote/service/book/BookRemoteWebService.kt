package com.jsanzo97.remote.service.book

import com.jsanzo97.remote.dto.response.book.BookResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface BookRemoteWebService {

    @GET("recipes")
    suspend fun getRecipes(@Query("username") username: String): Response<BookResponse>

    @DELETE("recipe/withName")
    suspend fun deleteRecipe(@Query("username") username: String, @Query("name") name: String): Response<Unit>

}