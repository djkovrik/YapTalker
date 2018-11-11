package com.sedsoftware.yaptalker.domain.device

interface UpdatesDownloader {
    fun initiateUpdateDownloadSession(url: String)
}
