package br.senai.sp.jandira.saf

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    private val URL_BASE = " http://10.0.2.2:3000/"
    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun Login(): Service {
        return retrofitFactory.create(Service::class.java)
    }

}