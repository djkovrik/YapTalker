package com.sedsoftware.yaptalker.data.network.site

import com.sedsoftware.yaptalker.data.network.site.model.FeedResult
import com.sedsoftware.yaptalker.data.network.site.model.SettingsResult
import com.sedsoftware.yaptalker.data.network.site.model.VoteResult
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PUT
import retrofit2.http.Query

interface YapApi {

    @Headers("Skip-Saved-Cookies: true")
    @GET("action/login")
    fun authUser(
        @Query("name") name: String,
        @Query("password") password: String
    ): Single<FeedResult>

    @GET("settings")
    fun settings(): Single<SettingsResult>

    @GET("action/logout")
    fun logout(): Single<FeedResult>

    @GET("feed/favs")
    fun loadFavoriteTopics(
        @Query("offset") offset: String
    ): Single<FeedResult>

    @PUT("action/favs")
    fun addFavorite(
        @Query("topic") topicId: String
    ): Single<Response<ResponseBody>>

    @DELETE("action/favs")
    fun removeFavorite(
        @Query("topic") topicId: String
    ): Single<Response<ResponseBody>>

    @GET("action/rank")
    fun vote(
        @Query("post") postId: String,
        @Query("value") value: String
    ): Single<VoteResult>

    @FormUrlEncoded
    @POST("action/comment")
    fun sendComment(
        @Field("topic") topicId: String,
        @Field("post") post: String,
        @Field("reply") replyPostId: String?
    ): Single<Response<ResponseBody>>

    @Multipart
    @POST("action/comment")
    fun sendComment(
        @Query("topic") topicId: String,
        @Part("post") post: RequestBody,
        @Query("reply") replyPostId: String?,
        @Part uploadedFile: MultipartBody.Part
    ): Single<Response<ResponseBody>>
}
