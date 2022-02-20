package com.amarchaud.composenetwork.data.api

import com.amarchaud.composenetwork.data.api.models.FolderContentApiModel
import com.amarchaud.composenetwork.data.api.models.MeApiModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

@JsonClass(generateAdapter = true)
data class CreateFolderCommand(
    @Json(name = "name")
    val name: String,
)

interface ComposeNetworkApi {
    @GET("/me")
    suspend fun getMyself(): Response<MeApiModel>

    @GET("/items/{id}")
    suspend fun getFolderContent(@Path("id") id: String): Response<List<FolderContentApiModel>>

    @POST("/items/{id}")
    @Headers("Content-Type: application/octet-stream")
    suspend fun postFile(
        @Header("Content-Disposition") name: String,
        @Path("id") folderId: String,
        @Body rawFile: RequestBody
    ): Response<FolderContentApiModel>

    @POST("/items/{id}")
    @Headers("Content-Type: application/json")
    suspend fun postFolder(
        @Path("id") folderId: String,
        @Body folderName: CreateFolderCommand
    ): Response<FolderContentApiModel>

    @DELETE("/items/{id}")
    suspend fun deleteById(@Path("id") id: String): Response<Unit>

    @GET("/items/{id}/data")
    suspend fun downloadById(@Path("id") id: String): Response<ResponseBody>

    @GET("/items/{id}/data")
    fun downloadByIdCall(@Path("id") id: String): Call<ResponseBody>
}