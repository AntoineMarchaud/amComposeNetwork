package com.amarchaud.composenetwork.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "me"
)
data class MeEntityModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id")
    val _id: Long = 0,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "firstName")
    var firstName: String? = null,
    @ColumnInfo(name = "lastName")
    var lastName: String? = null,
    @ColumnInfo(name = "id")
    var id: String? = null,
    @ColumnInfo(name = "parentId")
    var parentId: String? = null,
    @ColumnInfo(name = "isDir")
    var isDir: Boolean? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "modificationDate")
    var modificationDate: String? = null,
    @ColumnInfo(name = "size")
    var size: Int? = null,
    @ColumnInfo(name = "contentType")
    var contentType: String? = null
)