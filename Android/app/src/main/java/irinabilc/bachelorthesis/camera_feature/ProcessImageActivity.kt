package irinabilc.bachelorthesis.camera_feature

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import irinabilc.bachelorthesis.DataBinderMapperImpl
import irinabilc.bachelorthesis.ProcessImageActivityBinding
import irinabilc.bachelorthesis.R
import org.w3c.dom.Text
import java.util.*
import kotlin.text.Typography.paragraph

class ProcessImageActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ProcessImageActivityBinding
    private lateinit var processImgViewModel: ProcessImageViewModel
    private var tts: TextToSpeech? = null
    private lateinit var currentLanguage: String
    private val TAG = "ProcessImageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_process_image)
        processImgViewModel = ViewModelProviders.of(this).get(ProcessImageViewModel::class.java)

        processImgViewModel.setFilePath(intent.getStringExtra("file_path"))

//        val path = intent.getStringExtra("file_path")
//
//        binding.textView.text = ""
//
//        if (path != null) {
//            analyzeImage(path)
//        }
        processImgViewModel.getTextToBeDIsplayed()

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

        tts = TextToSpeech(this, this)

    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.UK)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "TTS:The Language specified is not supported!")
            }
        } else {
            Log.e(TAG, "TTS:Initialization Failed!")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_process_image, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == R.id.speak) {
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


//    private fun detectLanguage(text: String) {
//        val languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification()
//        languageIdentifier.identifyLanguage(text)
//            .addOnSuccessListener { language ->
//                if (!language.equals("und")) {
//                    currentLanguage = language
//                    setSpeakerLanguage()
//                } else {
//                    Log.e(TAG, "LanguageDetection: Cannot identify language")
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e(TAG, "LanguageDetection:Failure$e")
//            }
//    }

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
