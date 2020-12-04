package ru.mikov.test.data.remote

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import ru.mikov.test.data.remote.res.CardsRes


interface RestService {

    @GET("kmc.json")
    suspend fun getCards(): List<CardsRes>
}