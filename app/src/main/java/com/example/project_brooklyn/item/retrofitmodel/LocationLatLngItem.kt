package com.example.project_brooklyn.item.retrofitmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationLatLngItem(
	val latitude : Float,
	val longitude : Float
) : Parcelable