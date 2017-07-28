package com.sedsoftware.yaptalker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sedsoftware.yaptalker.data.remote.YapChosenTopicLoader
import com.sedsoftware.yaptalker.data.remote.converters.ChosenTopicConverterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val retrofit = Retrofit.Builder()
        .baseUrl("http://www.yaplakal.com/")
        .addConverterFactory(ChosenTopicConverterFactory())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val topicLoader = retrofit.create(YapChosenTopicLoader::class.java)

    topicLoader
        .loadChosenTopic(forumId = 1, startPage = 25, topicId = 1635125)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Timber.d("Loading started") }
        .doOnError { t -> Timber.d("Loading error: ${t.message}") }
        .doOnComplete { Timber.d("Loading completed") }
        .flatMap {list -> Observable.fromIterable(list) }
        .subscribe({ (id, author, date, uq) ->
            Timber.d("Post id: $id")
            Timber.d("Post author: ${author.name}")
            Timber.d("Post author id: ${author.id}")
            Timber.d("Post author avatar: ${author.avatar}")
            Timber.d("Post author desc: ${author.registered}")
            Timber.d("Post date: $date")
            Timber.d("Post rank: $uq")
        })

  }
}