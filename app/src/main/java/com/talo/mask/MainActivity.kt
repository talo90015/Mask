package com.talo.mask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getMaskData()
    }
}

private fun getMaskData() {
    //口罩資訊數來源
    val dataUrl = "https://raw.githubusercontent.com/thishkt/pharmacies/master/data/info.json"
    //宣告okHttpClient
    val okHttpClient = OkHttpClient().newBuilder().addInterceptor(
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )
    ).build()
    //宣告request 要求連接指定網址
    val request = Request.Builder().url(dataUrl).get().build()
    // 宣告cell
    val cell = okHttpClient.newCall(request)

    // Call 連線後，採用 enqueue 非同步方式
    cell.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("Talo", "onFailure: $e")
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("Talo", "Response: ${response.body?.string()}")
        }
    })

}
