package com.amarchaud.composenetwork.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.amarchaud.composenetwork.data.db.ComposeNetworkDao
import com.amarchaud.composenetwork.data.exception.NoConnectivityException
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val context: Context,
    private val dao: ComposeNetworkDao
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder()

        if (!isInternetAvailable()) {
            throw NoConnectivityException
        }

        runBlocking { dao.getUser() }?.let {
            newBuilder.apply {
                addHeader("Authorization", Credentials.basic(it.login, it.password))
            }
        }

        return chain.proceed(newBuilder.build())
    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}