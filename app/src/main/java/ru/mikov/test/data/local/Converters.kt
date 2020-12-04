package ru.mikov.test.data.local

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun timestampToDate(timestamp: Long): Date = Date(timestamp)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time
}

class ListConverter {
    @TypeConverter
    fun toList(str: String?): List<String> = str?.split(",") ?: emptyList()

    @TypeConverter
    fun fromList(list: List<String>?): String = list?.joinToString(separator = ",") ?: ""
}