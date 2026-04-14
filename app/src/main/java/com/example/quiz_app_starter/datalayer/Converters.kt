package com.example.quiz_app_starter.datalayer

import androidx.core.view.WindowInsetsCompat
import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun JsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}