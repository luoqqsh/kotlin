package comm.test.kotlin.api

import io.reactivex.Observable
import comm.test.kotlin.model.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Query



interface ApiService {

    //通用基础功能
    //登录
    @GET("login.xml")
    fun Version(@Query("name") name: String,
                @Query("pwd") pwd: String
    ): Observable<CommonResponse>




}