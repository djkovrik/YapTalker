package com.sedsoftware.yaptalker.presentation.feature.search

import android.annotation.SuppressLint
import android.os.Parcelable
import com.sedsoftware.yaptalker.presentation.feature.search.options.SearchConditions
import com.sedsoftware.yaptalker.presentation.feature.search.options.SortingMode
import com.sedsoftware.yaptalker.presentation.feature.search.options.TargetPeriod
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class SearchRequest(
    val searchFor: String,
    val targetForums: List<String> = emptyList(),
    val searchIn: String = "",
    val searchInTags: Boolean = false,
    @SearchConditions.Value val searchHow: String = SearchConditions.ALL_WORDS,
    @SortingMode.Value val sortBy: String = SortingMode.DATE,
    @TargetPeriod.Value val periodInDays: Long = TargetPeriod.ALL_TIME
) : Parcelable
