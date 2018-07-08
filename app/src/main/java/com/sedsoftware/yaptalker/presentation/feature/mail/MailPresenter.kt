package com.sedsoftware.yaptalker.presentation.feature.mail

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.network.site.YapLoaderAlpha
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MailPresenter @Inject constructor(
    private val api: YapLoaderAlpha
) : BasePresenter<MailView>() {

    override fun attachView(view: MailView?) {
        super.attachView(view)

        api.getMailInbox()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ inboxPage ->
                inboxPage.letters.forEach { letter ->
                    Timber.d("authorNickname = ${letter.authorNickname}")
//          Timber.d("authorLink = ${letter.authorLink}")
//          Timber.d("dateString = ${letter.dateString}")
//          Timber.d("isNew = ${letter.isNew}")
//          Timber.d("letterId = ${letter.letterId}")
//          Timber.d("letterLink = ${letter.letterLink}")
//          Timber.d("letterPreview = ${letter.letterPreview}")
                    Timber.d("---")
                }
            }, { e ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
