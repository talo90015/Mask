package com.talo.mask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.talo.mask.Data.MaskDataInfoNew
import com.talo.mask.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMaskData()

    }

    private fun getMaskData() {
        //口罩資訊數來源
        val dataUrl =
            "https://raw.githubusercontent.com/thishkt/pharmacies/fafd14667432171227be3e2461cf3b74f9cb9b67/data/info.json"

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
                binding.progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                val maskData = response.body?.string()
                // 使用Gson 解析Json資料
                val maskDataInfo = Gson().fromJson(maskData, MaskDataInfoNew::class.java)
                for (i in maskDataInfo.features) {
                    Log.d("Talo", "name: ${i.properties.name}, ${i.properties.phone}")
                }
                //執行在主執行緒中
                runOnUiThread {
                    binding.localMaskData.text = maskData
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }
}
