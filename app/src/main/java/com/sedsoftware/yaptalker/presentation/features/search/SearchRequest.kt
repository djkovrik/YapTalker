package com.sedsoftware.yaptalker.presentation.features.search

import android.annotation.SuppressLint
import android.os.Parcelable
import com.sedsoftware.yaptalker.presentation.features.search.options.SearchConditions
import com.sedsoftware.yaptalker.presentation.features.search.options.SortingMode
import com.sedsoftware.yaptalker.presentation.features.search.options.TargetPeriod
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class SearchRequest(
  val searchFor: String,
  val targetForums: List<String>,
  val searchIn: String,
  @SearchConditions.Value val searchHow: String,
  @SortingMode.Value val sortBy: String,
  @TargetPeriod.Value val periodInDays: Long
) : Parcelable
