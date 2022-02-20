package com.amarchaud.composenetwork.ui.screen.navigate.model

import com.amarchaud.composenetwork.domain.models.FolderContentModel

fun FolderContentModel.toFolderUiModel(): FolderUiModel {
    return FolderUiModel(
        id = this.id,
        parentId = this.parentId,
        name = this.name,
    )
}

fun FolderContentModel.toFileUiModel(): FileUiModel {
    return FileUiModel(
        id = this.id,
        parentId = this.parentId,
        name = this.name,
        modificationDate = this.modificationDate.toString(),
        size = this.size,
        contentType = this.contentType
    )
}

fun List<FolderContentModel>.toUiModel(
    title: String,
    currentId: String,
    parentId: String?
): FolderContentUiModel {

    val group = this.groupBy { it.isDir }

    return FolderContentUiModel(
        title = title,
        parentId = parentId,
        currentId = currentId,
        folders = group[true]?.sortedBy { it.name }?.map { it.toFolderUiModel() }?.toMutableList() ?: mutableListOf(),
        files = group[false]?.sortedBy { it.name }?.map { it.toFileUiModel() }?.toMutableList() ?: mutableListOf()
    )
}