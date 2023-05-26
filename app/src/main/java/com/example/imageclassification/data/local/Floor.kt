package com.example.imageclassification.data.local

data class Floor(
    var id: Int,
    var name: String,
    var halls: MutableList<Hall>,
    var buildingId: Int,
)
