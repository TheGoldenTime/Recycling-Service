package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Participant (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val status: String = "",
    val image: String = ""): Parcelable
