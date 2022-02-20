package com.amarchaud.composenetwork.domain.models

import org.threeten.bp.LocalDateTime

data class FolderContentModel(
    val id: String,
    val parentId: String?,
    val isDir: Boolean,
    val name: String,
    val modificationDate: LocalDateTime,
    val size: Int?,
    val contentType: String?
)