package com.amarchaud.composenetwork.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FolderContentApiModel(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "parentId")
    val parentId: String?,
    @field:Json(name = "isDir")
    val isDir: Boolean,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "modificationDate")
    val modificationDate: String,
    @field:Json(name = "size")
    val size: Int?,
    @field:Json(name = "contentType")
    val contentType: String?
)