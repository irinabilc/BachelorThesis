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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProcessImageViewModel : ViewModel() {
    private var filePath: String = ""
    val textToDisplay = MutableLiveData<String>()
    val textLanguage = MutableLiveData<Locale>()


    fun setFilePath(file: String) {
        this.filePath = file
    }

    fun getTextLanguage(text: String?){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                detectLanguage(text)
            }
        }
    }

    fun getTextToBeDisplayed() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                analyzeImage()
            }
        }
    }

//    private fun processImage() {
//        val imageProcessor = ImageProcessor()
//        textToDisplay.postValue(imageProcessor.getText(filePath))
//    }

    private fun analyzeImage() {
        val bitmap = BitmapFactory.decodeFile(filePath)

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector = FirebaseVision.getInstance()
            .cloudDocumentTextRecognizer

        detector.processImage(image).addOnSuccessListener { texts ->
            processTextRecognitionResult(texts)
        }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }


    private fun processTextRecognitionResult(texts: FirebaseVisionDocumentText?) {
        val blocks = texts!!.blocks
        val stringBuilder = StringBuilder()
        if (blocks.size == 0) {
            return
        }
        blocks.forEach { block ->
            block.paragraphs.forEach { paragraph ->
                stringBuilder.append(paragraph.text.toString()).append(" ")

            }
        }
        textToDisplay.value = stringBuilder.toString()
    }

//    private fun translateLanguage()

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