package com.example.imageclassification.data.local

data class Hall(
    var id: Int,
    var name: String,
    var imgs: MutableList<Int>,
    var buildingId: Int,
    var FloorId: Int,
)
