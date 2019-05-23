package cespresso.gmail.com.espressosar.data.source

import cespresso.gmail.com.espressosar.data.entity.Sign
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface IApiService{
    @GET("/getAllSigns")
    fun getAllSigns(): Deferred<Response<MutableList<Sign>>>
}
