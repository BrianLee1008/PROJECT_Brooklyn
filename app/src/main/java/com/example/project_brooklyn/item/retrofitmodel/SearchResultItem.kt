package com.example.project_brooklyn.item.retrofitmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SearchResultItem(
	val fullAddress: String,
	val locationName: String,
	val locationLatLngItem: LocationLatLngItem
) : Parcelable