package com.example.quiz_app_starter.datalayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.quiz_app_starter.model.Question
import dagger.hilt.android.internal.Contexts

@Database(
    entities = [Question::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizDB : RoomDatabase() {
    abstract val questionDao: QuestionDao

    companion object{
        @Volatile
        private var instance: QuizDB? = null

        fun getDatabase(context: Context): QuizDB{
            return instance ?:synchronized(this) {
                Room.databaseBuilder(context, QuizDB::class.java, "question_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}