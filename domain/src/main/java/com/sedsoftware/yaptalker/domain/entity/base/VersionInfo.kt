package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

class VersionInfo(
    val versionCode: Int = 0,
    val versionName: String = "",
    val downloadLink: String = ""
) : BaseEntity
