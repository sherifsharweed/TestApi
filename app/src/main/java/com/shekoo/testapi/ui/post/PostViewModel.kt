package com.shekoo.testapi.ui.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.shekoo.testapi.utility.Header
import com.shekoo.testapi.utility.PostBody
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class PostViewModel : ViewModel() {

    private var _postResponse: MutableLiveData<String> = MutableLiveData()
    var postResponse: LiveData<String> = _postResponse

    fun postMethod(url: String, bodyList: List<PostBody> , headerList : List<Header>) {

        val jsonObject = JSONObject()
        for (item in bodyList) {
            jsonObject.put(item.key, item.value)
        }
        val jsonObjectString = jsonObject.toString()

        val httpURLConnection = makeHttpUrlConnection(url, jsonObjectString , headerList)
        val responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            _postResponse.postValue(collectResponse(httpURLConnection))
            postResponse = _postResponse
        } else {
            _postResponse.postValue(responseCode.toString())
        }
        httpURLConnection.disconnect()
    }

    private fun makeHttpUrlConnection(url: String, jsonObjectString: String , headerList: List<Header>): HttpURLConnection {
        val httpURLConnection = URL(url).openConnection() as HttpURLConnection
        for(item in headerList){
            httpURLConnection.setRequestProperty(item.key,item.value)
        }
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = true

        val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
        outputStreamWriter.write(jsonObjectString)
        outputStreamWriter.flush()

        return httpURLConnection
    }

    private fun collectResponse(httpURLConnection: HttpURLConnection): String {
        val result: StringBuilder = StringBuilder()
        val response = httpURLConnection.inputStream.bufferedReader()
            .use { it.readText() }
        // Get headers in response
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