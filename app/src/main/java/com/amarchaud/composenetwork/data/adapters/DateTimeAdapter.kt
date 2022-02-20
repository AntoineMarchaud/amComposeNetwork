package com.amarchaud.composenetwork.data.adapters

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

sealed class DateTimeAdapter {

    /* DB */
    object LocalDateDbConverter : DateTimeAdapter() {

        var formatter = DateTimeFormatter.ISO_DATE_TIME

        @TypeConverter
        fun localDateToString(localDate: LocalDateTime?): String? { // call when insert data in db
            return localDate?.let { formatter.format(it) }
        }

        @TypeConverter
        fun stringToLocalDate(str: String?): LocalDateTime? { // call when loading db
            return str?.let { LocalDateTime.parse(it, formatter) }
        }
    }

    object LocalDateTimeAdapter : DateTimeAdapter() {


        var formatter = DateTimeFormatter.ISO_DATE_TIME

        @ToJson
        fun toJson(localDate: LocalDateTime): String? {
            return formatter.format(localDate)
        }

        @FromJson
        fun fromJson(json: String): LocalDateTime { // from api to me
            return LocalDateTime.parse(json, formatter)
        }
    }
}