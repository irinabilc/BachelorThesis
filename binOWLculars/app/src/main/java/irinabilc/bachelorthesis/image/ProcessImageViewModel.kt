package irinabilc.bachelorthesis.image

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentificationOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import irinabilc.bachelorthesis.model.TextEntry
import irinabilc.bachelorthesis.storage.TextEntryController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProcessImageViewModel(private val controller: TextEntryController) : ViewModel() {
    private var filePath: String = ""
    val textToDisplay = MutableLiveData<String>()
    val textLanguage = MutableLiveData<Locale>()


    fun setFilePath(file: String) {
        this.filePath = file
    }

    fun getTextLanguage(text: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                detectLanguage(text)
            }
        }
    }

    fun getTextToBeDisplayed(isConnected: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                analyzeImage(isConnected)
            }
        }
    }

    fun save(name: String) {

        viewModelScope.launch {
            controller.addEntry(
                name,
                textLanguage.value!!.toLanguageTag(),
                textToDisplay.value.toString(),
                filePath
            )
        }
    }


    private fun analyzeImage(isConnected: Boolean) {
        val bitmap = BitmapFactory.decodeFile(filePath)

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector: FirebaseVisionTextRecognizer
        if (isConnected) {
            detector = FirebaseVision.getInstance()
                .cloudTextRecognizer
        } else {
            detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        }

        detector.processImage(image).addOnSuccessListener { texts ->
            processTextRecognitionResult(texts)
        }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }


    private fun processTextRecognitionResult(texts: FirebaseVisionText?) {
        val blocks = texts!!.textBlocks
        val stringBuilder = StringBuilder()
        if (blocks.size == 0) {
            return
        }
        blocks.forEach { block ->
            block.lines.forEach { line ->
                stringBuilder.append(line.text.plus(" "))

            }
        }
        textToDisplay.value = stringBuilder.toString()
    }


    private fun detectLanguage(textInput: String?) {

        val text = textInput.toString().toLowerCase()

        val languageIdentifier = FirebaseNaturalLanguage.getInstance().languageIdentification
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { language ->
                if (language != "und") {
                    textLanguage.value = Locale.forLanguageTag(language)
                } else {
                    Log.e("processImgViewModel", "LanguageDetection: Cannot identify language")
                }
            }
            .addOnFailureListener { e ->
                Log.e("processImgViewModel", "LanguageDetection:Failure$e")
            }
    }
}