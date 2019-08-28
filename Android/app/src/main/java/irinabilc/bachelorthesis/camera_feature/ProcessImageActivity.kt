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
    private var tts: TextToSpeech? = null
    private lateinit var currentLanguage: String
    private val TAG = "ProcessImageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_process_image)

        val path = intent.getStringExtra("file_path")

        binding.textView.text = ""

        if (path != null) {
            analyzeImage(path)
        }

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


    private fun analyzeImage(path: String?) {
        val bitmap = BitmapFactory.decodeFile(path)

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector = FirebaseVision.getInstance()
            .cloudDocumentTextRecognizer

        detector.processImage(image).addOnSuccessListener { texts ->
            processTextRecognitionResult(texts)
        }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error, please retry!", Toast.LENGTH_SHORT).show()
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

    private fun processTextRecognitionResult(texts: FirebaseVisionDocumentText?) {
        val blocks = texts!!.blocks
        var textBuilder = ""
        if (blocks.size == 0) {
            Toast.makeText(this, "No content detected", Toast.LENGTH_SHORT).show()
            return
        }
        blocks.forEach { block ->
            block.paragraphs.forEach { paragraph ->
                binding.textView.append(paragraph.text.toString())
            }
        }

        //binding.textView.setText(textBuilder)
        //detectLanguage(textBuilder)
    }

    private fun detectLanguage(text: String) {
        val languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification()
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { language ->
                if (!language.equals("und")) {
                    currentLanguage = language
                    setSpeakerLanguage()
                } else {
                    Log.e(TAG, "LanguageDetection: Cannot identify language")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "LanguageDetection:Failure$e")
            }
    }

    private fun setSpeakerLanguage() {
        tts?.setLanguage(Locale.forLanguageTag(currentLanguage))
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
