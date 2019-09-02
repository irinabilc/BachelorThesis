package irinabilc.bachelorthesis.image

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import irinabilc.bachelorthesis.MainActivity
import irinabilc.bachelorthesis.ProcessImageActivityBinding
import irinabilc.bachelorthesis.language.TTSListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import android.text.method.ScrollingMovementMethod
import irinabilc.bachelorthesis.R


class ProcessImageActivity : AppCompatActivity() {

    private lateinit var binding: ProcessImageActivityBinding
    private val processImgViewModel: ProcessImageViewModel  by viewModel()
    private var tts: TextToSpeech? = null
    private lateinit var currentLanguage: String
    private val TAG = "ProcessImageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_process_image)
        binding.lifecycleOwner=this

        binding.textView.movementMethod = ScrollingMovementMethod()

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting ?: false

        processImgViewModel.setFilePath(intent.getStringExtra("file_path"))

        processImgViewModel.getTextToBeDisplayed(isConnected)

        processImgViewModel.textToDisplay.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                binding.textView.text = it
                processImgViewModel.getTextLanguage(it.toString())
            }
        })

        processImgViewModel.textLanguage.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                setSpeakerLanguage(it)
            }
        })

        tts = TextToSpeech(this, TTSListener())

        binding.saveButton.setOnClickListener {
            processImgViewModel.save()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_process_image, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == R.id.speak) {
            if (tts!!.isSpeaking) {
                tts!!.stop()
                return true
            }
            speakOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun speakOut() {

        val message = binding.textView.text.toString()
        if (message.isEmpty() || message.isBlank()) {
            Toast.makeText(this, "Content not available", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Text not available")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            @Suppress("DEPRECATION")
            tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null)
        }
    }


    private fun setSpeakerLanguage(language: Locale) {
        val result = tts!!.setLanguage(language)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "TTS:The Language specified is not supported!")
        }
    }

    override fun onDestroy() {
        // Shut down TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }

    companion object {
        private val IMAGE_FILE_PATH = "file_path"
        fun getStartIntent(context: Context, path: String): Intent {
            val intent = Intent(context, ProcessImageActivity::class.java)
            intent.putExtra(IMAGE_FILE_PATH, path)
            return intent
        }
    }
}
