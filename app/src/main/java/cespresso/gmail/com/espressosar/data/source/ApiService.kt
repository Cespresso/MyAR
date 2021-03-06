package cespresso.gmail.com.espressosar.data.source

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiService() {

    val service = create(IApiService::class.java)


    fun <S> create(serviceClass: Class<S>): S {
        // create retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://us-central1-arcore-sign.cloudfunctions.net")
            .client(httpBuilder().build())
            .build()

        return retrofit.create(serviceClass)
    }

    private fun httpBuilder(): OkHttpClient.Builder {
        // create http client
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()

                //header
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build()

                return@Interceptor chain.proceed(request)
            })
            .readTimeout(5, TimeUnit.SECONDS)


        // log interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(loggingInterceptor)

        //auth interceptor


        return httpClient
    }

}