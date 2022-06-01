package com.shekoo.testapi.ui.get

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class GetViewModel : ViewModel() {

    private var _getResponse : MutableLiveData<String> = MutableLiveData()
    var getResponse : LiveData<String> = _getResponse

    fun getMethod(url : String) {

        val httpURLConnection = makeHttpUrlConnection(url)
        val responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            _getResponse.postValue(collectResponse(httpURLConnection))
            getResponse = _getResponse
        } else {
            Log.i("TAG", responseCode.toString())
            _getResponse.postValue(responseCode.toString())
        }
        httpURLConnection.disconnect()
    }

    private fun makeHttpUrlConnection(url: String) : HttpURLConnection {
        val httpURLConnection = URL(url).openConnection() as HttpURLConnection
        /*httpURLConnection.setRequestProperty(
            "Accept",
            "application/json"
        )*/
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = false
        return httpURLConnection
    }

    private fun collectResponse(httpURLConnection: HttpURLConnection) : String {
        val result : StringBuilder = StringBuilder()
        val response = httpURLConnection.inputStream.bufferedReader()
            .use { it.readText() }
        // Get headers in response
        val map: Map<String, List<String>> = httpURLConnection.headerFields
        for ((key, value) in map) {
            result.append("\n")
            if (key.isNullOrEmpty()) {
                result.append(value)
            } else {
                result.append(key).append(value)
            }
        }
        // Convert raw JSON to pretty JSON using GSON library
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(JsonParser.parseString(response))
        result.append(prettyJson)
        return result.toString()
    }


}