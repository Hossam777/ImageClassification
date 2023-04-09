package com.example.imageclassification.data.local

import android.graphics.Bitmap

data class Building(
    val id: Int,
    val name: String,
    val description: String,
    val photo: Int,
    var photoBitmap: Bitmap?,
)
