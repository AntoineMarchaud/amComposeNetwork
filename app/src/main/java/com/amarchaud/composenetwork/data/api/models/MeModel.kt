package com.amarchaud.composenetwork.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass




@JsonClass(generateAdapter = true)
data class MeApiModel(
    @field:Json(name = "firstName")
    val firstName: String,
    @field:Json(name = "lastName")
    val lastName: String,
    @field:Json(name = "rootItem")
    val rootItem: FolderContentApiModel
)

