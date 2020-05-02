package com.example.noteapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext
import java.util.concurrent.locks.Lock

@Database(
     entities = [Note::class],
    version = 3
)
abstract class NoteDatabase :RoomDatabase(){

    abstract fun getNoteDao() : NoteDao

    companion object{
        @Volatile private var instance : NoteDatabase? = null
        private val Lock= Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock){
            instance ?: buildDatabase(context).also {
                instance=it
            }
        }

        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "notedatabase"
        ).fallbackToDestructiveMigration().build()
    }
}