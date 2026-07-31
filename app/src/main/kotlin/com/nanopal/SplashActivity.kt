package com.nanopal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nanopal.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val modelFileName = "llama-3.2-1b-instruct-q4_k_m.gguf"
    // URL de ejemplo (Hugging Face)
    private val modelUrl = "https://huggingface.co/bartowski/Llama-3.2-1B-Instruct-GGUF/resolve/main/Llama-3.2-1B-Instruct-Q4_K_M.gguf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndDownloadModel()
    }

    private fun checkAndDownloadModel() {
        val modelFile = File(filesDir, modelFileName)
        if (modelFile.exists()) {
            startMainActivity()
        } else {
            downloadModel(modelFile)
        }
    }

    private fun downloadModel(targetFile: File) {
        binding.statusText.text = getString(R.string.downloading_model)
        binding.downloadProgress.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL(modelUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val fileLength = connection.contentLength
                val input = connection.inputStream
                val output = FileOutputStream(targetFile)

                val data = ByteArray(4096)
                var total: Long = 0
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    if (fileLength > 0) {
                        val progress = (total * 100 / fileLength).toInt()
                        withContext(Dispatchers.Main) {
                            binding.downloadProgress.progress = progress
                        }
                    }
                    output.write(data, 0, count)
                }

                output.flush()
                output.close()
                input.close()

                withContext(Dispatchers.Main) {
                    startMainActivity()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.statusText.text = "Error al descargar: ${e.message}"
                }
            }
        }
    }

    private fun startMainActivity() {
        lifecycleScope.launch {
            binding.statusText.text = getString(R.string.loading_model)
            delay(1000) // Simulación de carga
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}
