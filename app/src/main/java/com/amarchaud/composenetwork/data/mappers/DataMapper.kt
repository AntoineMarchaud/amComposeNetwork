package com.amarchaud.composenetwork.data.mappers

import com.amarchaud.composenetwork.data.adapters.DateTimeAdapter
import com.amarchaud.composenetwork.data.api.models.FolderContentApiModel
import com.amarchaud.composenetwork.data.api.models.MeApiModel
import com.amarchaud.composenetwork.data.db.models.MeEntityModel
import com.amarchaud.composenetwork.domain.models.MeModel
import com.amarchaud.composenetwork.domain.models.FolderContentModel
import org.threeten.bp.LocalDateTime

fun FolderContentApiModel.toDomain() = FolderContentModel(
    id = this.id,
    parentId = this.parentId,
    isDir = this.isDir,
    name = this.name,
    modificationDate = DateTimeAdapter.LocalDateTimeAdapter.fromJson(this.modificationDate),
    size = this.size,
    contentType = this.contentType
)

fun MeApiModel.toDomain() = MeModel(
    firstName = this.firstName,
    lastName = this.lastName,
    rootItem = this.rootItem.toDomain()
)

fun MeEntityModel.toDomain() = MeModel(
    firstName = this.firstName ?: "",
    lastName = this.lastName ?: "",
    rootItem = FolderContentModel(
        id = this.id ?: "",
        parentId = this.parentId,
        isDir = this.isDir ?: false,
        name = this.name ?: "",
        modificationDate = this.modificationDate?.let {
            DateTimeAdapter.LocalDateTimeAdapter.fromJson(
                it
            )
        } ?: LocalDateTime.MAX,
        size = this.size,
        contentType = this.contentType
    )
)


