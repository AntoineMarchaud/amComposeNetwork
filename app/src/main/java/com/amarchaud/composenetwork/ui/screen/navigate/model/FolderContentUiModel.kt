package com.amarchaud.composenetwork.ui.screen.navigate.model


data class FolderUiModel(
    val id: String,
    val parentId: String? = null,
    val name: String
)

data class FileUiModel(
    val id: String,
    val parentId: String?,
    val name: String,
    val modificationDate: String,
    val size: Int? = null,
    val contentType: String? = null
)


data class FolderContentUiModel(
    val title: String,
    val currentId: String,
    val parentId: String?, // null = top parent
    val folders: MutableList<FolderUiModel>,
    val files: MutableList<FileUiModel>
)

