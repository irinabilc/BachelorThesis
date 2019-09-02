package irinabilc.bachelorthesis.language

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import org.w3c.dom.Text
import java.util.*

class TTSListener : TextToSpeech.OnInitListener {
    private val tts: TextToSpeech? = null
    private val TAG = "TTS"

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.UK)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "The Language specified is not supported!")
            }
        } else {
            Log.e(TAG, "Initialization Failed!")
        }
    }

}