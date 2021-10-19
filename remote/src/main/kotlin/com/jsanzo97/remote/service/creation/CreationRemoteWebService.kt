package com.jsanzo97.remote.service.creation

import com.jsanzo97.remote.dto.request.creation.CreationRecipeRequest
import com.jsanzo97.remote.dto.response.creation.CategoriesTypesResponse
import com.jsanzo97.remote.dto.response.creation.DifficultTypesResponse
import com.jsanzo97.remote.dto.response.creation.IngredientTypesResponse
import com.jsanzo97.remote.dto.response.creation.SubcategoriesTypesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CreationRemoteWebService {

    @GET("recipes/categories")
    suspend fun getCategories(): Response<CategoriesTypesResponse>

    @GET("recipes/subcategories")
    suspend fun getSubcategories(): Response<SubcategoriesTypesResponse>

    @GET("recipes/ingredientTypes")
    suspend fun getIngredientTypes(): Response<IngredientTypesResponse>

    @GET("recipes/difficulties")
    suspend fun getDifficulties(): Response<DifficultTypesResponse>

    @POST("recipe")
    suspend fun createRecipe(@Query("username") username: String, @Body request: CreationRecipeRequest): Response<Unit>

}