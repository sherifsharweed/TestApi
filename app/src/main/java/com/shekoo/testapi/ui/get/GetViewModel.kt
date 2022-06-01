package com.shekoo.testapi.ui.get

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.shekoo.testapi.utility.Header
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class GetViewModel : ViewModel() {

    private var _getResponse: MutableLiveData<String> = MutableLiveData()
    var getResponse: LiveData<String> = _getResponse

    fun getMethod(url: String, headerList: List<Header>) {

        val httpURLConnection = makeHttpUrlConnection(url, headerList)
        val responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            _getResponse.postValue(collectResponse(httpURLConnection))
            getResponse = _getResponse
        } else {
            _getResponse.postValue(responseCode.toString())
        }
        httpURLConnection.disconnect()
    }

    private fun makeHttpUrlConnection(url: String, headerList: List<Header>): HttpURLConnection {

        val httpURLConnection = URL(url).openConnection() as HttpURLConnection
        for (item in headerList) {
            httpURLConnection.setRequestProperty(item.key, item.value)
        }
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = false
        return httpURLConnection
    }

    private fun collectResponse(httpURLConnection: HttpURLConnection): String {

        val result: StringBuilder = StringBuilder()
        val response = httpURLConnection.inputStream.bufferedReader()
            .use { it.readText() }
        val map: Map<String?, List<String>> = httpURLConnection.headerFields
        for ((key, value) in map) {
            result.append("\n")
            if (key.isNullOrEmpty()) {
                result.append(value)
            } else {
                result.append(key).append(value)
            }
        }
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(JsonParser.parseString(response))
        result.append(prettyJson)
        return result.toString()
    }


}