package com.nanopal

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nanopal.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var speechManager: SpeechManager
    private lateinit var ttsManager: TtsManager
    private lateinit var llamaEngine: LlamaEngine
    private lateinit var memoryManager: MemoryManager
    
    private var talkAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initManagers()
        setupListeners()
    }

    private fun initManagers() {
        memoryManager = MemoryManager(this)
        llamaEngine = LlamaEngine(this)
        
        // Cargar modelo
        lifecycleScope.launch {
            val modelPath = File(filesDir, "llama-3.2-1b-instruct-q4_k_m.gguf").absolutePath
            llamaEngine.loadModel(modelPath)
        }

        speechManager = SpeechManager(this, 
            onResult = { text -> processInput(text) },
            onStateChange = { isListening -> updateListeningUI(isListening) }
        )

        ttsManager = TtsManager(this, 
            onStateChange = { isTalking -> updateTalkingUI(isTalking) }
        )
    }

    private fun setupListeners() {
        binding.micButton.setOnClickListener {
            speechManager.startListening()
        }
    }

    private fun updateListeningUI(isListening: Boolean) {
        if (isListening) {
            binding.avatarImage.setImageResource(R.drawable.nanopal_listening)
            binding.responseText.text = getString(R.string.listening)
        } else {
            // No cambiamos a avatar normal todavía, esperamos el procesamiento
        }
    }

    private fun processInput(text: String) {
        lifecycleScope.launch {
            binding.responseText.text = getString(R.string.processing)
            
            val memory = memoryManager.readMemory()
            val response = llamaEngine.generateResponse(text, memory)
            
            // Guardar algo en memoria (simulado)
            memoryManager.appendFact("El usuario dijo: $text")
            
            binding.responseText.text = response
            ttsManager.speak(response)
        }
    }

    private fun updateTalkingUI(isTalking: Boolean) {
        if (isTalking) {
            binding.avatarImage.setImageResource(R.drawable.nanopal_talking)
            startTalkingAnimation()
        } else {
            binding.avatarImage.setImageResource(R.drawable.nanopal_avatar)
            stopTalkingAnimation()
        }
    }

    private fun startTalkingAnimation() {
        talkAnimator = ObjectAnimator.ofFloat(binding.avatarImage, "scaleX", 1f, 1.2f).apply {
            duration = 300
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(binding.avatarImage, "scaleY", 1f, 1.2f).apply {
            duration = 300
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun stopTalkingAnimation() {
        talkAnimator?.cancel()
        binding.avatarImage.scaleX = 1f
        binding.avatarImage.scaleY = 1f
    }

    override fun onDestroy() {
        super.onDestroy()
        speechManager.destroy()
        ttsManager.destroy()
    }
}
