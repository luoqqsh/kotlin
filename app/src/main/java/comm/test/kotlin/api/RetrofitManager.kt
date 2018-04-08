package comm.test.kotlin.api

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import comm.test.kotlin.tools.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitManager::class.java) {
                if (retrofit == null) {
//                    val interceptor2 = LoggingInterceptor()
//                    interceptor2.level = LoggingInterceptor.Level.BODY
                    //设置 请求的缓存的大小跟位置
//                    val cacheFile = File(YLApplication.context.cacheDir, "cache")
//                    val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小
                    client = OkHttpClient.Builder()
//                            .cache(cache)  //添加缓存
                            .connectTimeout(60L, TimeUnit.SECONDS)
                            .readTimeout(60L, TimeUnit.SECONDS)
                            .writeTimeout(60L, TimeUnit.SECONDS)
                            .addInterceptor(Interceptor { chain ->
                                val newRequest = chain.request().newBuilder()
                                        .addHeader("Authorization", "Basic " + Base64.encode(Constant.basekey))
                                        .addHeader("AcceptLanguage", "zh-cn").build()
                                chain.proceed(newRequest)
                            })
                            .addInterceptor(initLogInterceptor()) //日志,所有的请求响应度看到
                            .build()

                    // 获取retrofit的实例
                    retrofit = Retrofit.Builder()
                            .baseUrl(Constant.dev_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(SimpleXmlConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(client!!)
                            .build()
                }
            }
        }
        return retrofit
    }

    private fun initLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("info", "请求参数:" + message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}
