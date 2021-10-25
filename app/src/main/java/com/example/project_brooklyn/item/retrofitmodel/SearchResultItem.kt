package com.example.project_brooklyn.item.retrofitmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SearchResultItem(
	val fullAddress: String,
	val locationName: String,
	val telNumber: String,
	val upperBizName: String,
	val middleBizName: String,
	val lowerBizName: String,
	val desc: String,
	val locationLatLngItem: LocationLatLngItem
) : Parcelable