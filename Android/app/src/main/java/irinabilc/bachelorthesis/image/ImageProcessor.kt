package irinabilc.bachelorthesis.image

import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText

class ImageProcessor {
    private val TAG = "ImageProcessor"

    fun analyzeImage(filePath: String) {
        val bitmap = BitmapFactory.decodeFile(filePath)

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector = FirebaseVision.getInstance()
            .cloudDocumentTextRecognizer

        detector.processImage(image).addOnSuccessListener { texts ->
            processTextRecognitionResult(texts)

        }
            .addOnFailureListener { e ->
                Log.e(TAG, "Firebase Vision Text Recognizer failure$e")
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
    }
}