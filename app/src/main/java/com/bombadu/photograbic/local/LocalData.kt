package com.bombadu.photograbic.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class LocalData(
    var userUrl: String,
    var largeImageUrl: String,
    var webImageUrl: String,
    var username: String,
    var isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)
