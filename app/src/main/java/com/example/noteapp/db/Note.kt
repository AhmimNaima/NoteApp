package com.example.noteapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note (

    //@ColumnInfo(name="note_title")
    val title: String,
    val note: String,
    val date: String
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id :Int=0
}