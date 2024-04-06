package com.example.credibanco.ui.theme.Core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PaymentApiClient {
     fun postPaymentAuthorization(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.39.165:8080/api/payments/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}