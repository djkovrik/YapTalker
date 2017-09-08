package com.sedsoftware.yaptalker.di.modules

//@Module
//class RetrofitModule {
//
//  companion object {
//    private const val YAPLAKAL_ENDPOINT = "http://www.yaplakal.com/"
//    private const val RUTUBE_ENDPOINT = "http://rutube.ru/"
//    private const val COUB_ENDPOINT = "http://coub.com/"
//  }
//
//  @Provides
//  @Singleton
//  @Named("YapLoader")
//  fun provideRetrofitYapLoader(): Retrofit {
//    return Retrofit.Builder()
//        .baseUrl(YAPLAKAL_ENDPOINT)
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addConverterFactory(JspoonConverterFactory.create())
//        .build()
//  }
//
//  @Provides
//  @Singleton
//  @Named("RutubeThumbnailLoader")
//  fun provideRetrofitRutubeThumbnail(): Retrofit {
//    return Retrofit.Builder()
//        .baseUrl(RUTUBE_ENDPOINT)
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addConverterFactory(MoshiConverterFactory.create())
//        .build()
//  }
//
//  @Provides
//  @Singleton
//  @Named("CoubThumbnailLoader")
//  fun provideRetrofitCoubThumbnail(): Retrofit {
//    return Retrofit.Builder()
//        .baseUrl(COUB_ENDPOINT)
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addConverterFactory(MoshiConverterFactory.create())
//        .build()
//  }
//}
