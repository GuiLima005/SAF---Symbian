package br.senai.sp.jandira.saf

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {

    @POST("usuario/cadastrarUsuario")
    fun insertLogin(@Body login: Response): Call<ResponseBody>

}