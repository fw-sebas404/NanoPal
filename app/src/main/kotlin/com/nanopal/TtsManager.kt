package com.nanopal

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class TtsManager(
    private val context: Context,
    private val onStateChange: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    onStateChange(true)
                }
                override fun onDone(utteranceId: String?) {
                    onStateChange(false)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    onStateChange(false)
                }
            })
        }
    }

    fun speak(text: String) {
        val params = android.os.Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "nanopal_speech")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "nanopal_speech")
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
    }
}
