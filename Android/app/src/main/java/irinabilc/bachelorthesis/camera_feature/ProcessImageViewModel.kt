package irinabilc.bachelorthesis.camera_feature

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText

class ProcessImageViewModel() : ViewModel(){
    private var filePath: String = ""
    private var textToDisplay: StringBuilder? = null

    fun setFilePath(file: String){
        this.filePath = file
    }

    fun getTextToBeDIsplayed(): StringBuilder?{
        return this.textToDisplay
    }


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
        this.textToDisplay = stringBuilder
    }
}