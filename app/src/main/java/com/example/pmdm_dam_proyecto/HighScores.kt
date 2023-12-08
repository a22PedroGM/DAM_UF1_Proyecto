package com.example.pmdm_dam_proyecto

import androidx.room.Entity
import androidx.room.PrimaryKey
data class HighScores(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val score: Int
)
