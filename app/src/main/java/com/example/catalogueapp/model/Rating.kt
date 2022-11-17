package com.example.catalogueapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val rate: Double,
    val count: Int
) : Parcelable