package irinabilc.bachelorthesis.camera_feature

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import irinabilc.bachelorthesis.CameraActivityBinding
import irinabilc.bachelorthesis.R
import irinabilc.bachelorthesis.domain.Image
import irinabilc.bachelorthesis.networking.ApiNetworkInterface
import irinabilc.bachelorthesis.networking.ServiceGenerator
import kotlinx.android.synthetic.main.activity_camera.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {
    private lateinit var _cameraViewFinder: TextureView
    private lateinit var _button: FloatingActionButton

    private val REQUEST_CODE_PERMISSIONS = 10

    // This is an array of all the permission specified in the manifest
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)


//    private var photoFile: File? = null
//    private lateinit var currentPhotoPath: String
//    private val DIRECTORY_NAME = "Licenta"

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<CameraActivityBinding>(this, R.layout.activity_camera)

        _cameraViewFinder = binding.viewFinder
        //_TextView = binding.imageText
        _button = binding.captureButton

        if (allPermissionsGranted()) {
            _cameraViewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Every time the provided texture view changes, recompute layout
        _cameraViewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }


        //_TextView.isVisible = false


//        val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)
//        _button.setOnClickListener {
//            service.addImage(Image("borhot")).enqueue(object : Callback<Boolean>{
//                override fun onFailure(call: Call<Boolean>, t: Throwable) {
//                    Log.d("OnFailureError", t.message.toString())
//                }
//
//                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
//                    Log.d("OnSuccess", "Yay!")
//                }
//            })
//        }

    }

    private fun startCamera() {
        // TODO: Implement CameraX operations
    }

    private fun updateTransform() {
        // TODO: Implement camera viewfinder transformations
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                _cameraViewFinder.post { startCamera() }
            } else {
                displayMessage(
                    this,
                    "Permissions not granted by the user."
                )
                finish()
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

//
//    private fun captureImage() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                0
//            )
//        } else {
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if (cameraIntent.resolveActivity(packageManager) != null) {
//                try {
//                    photoFile = createImageFile()
//                    displayMessage(baseContext, photoFile!!.absolutePath.toString())
//
//                    if (photoFile != null) {
//
//                        val photoUri =
//                            FileProvider.getUriForFile(
//                                baseContext,
//                                "irinabilc.bachelorthesis.fileprovider",
//                                photoFile!!
//                            )
//
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST)
//                    }
//                } catch (ex: Exception) {
//                    displayMessage(baseContext, "Capture image exception: " + ex.printStackTrace())
//                }
//            } else {
//                displayMessage(baseContext, "Null!")
//            }
//
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//            val bitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
//            _PhotoView.setImageBitmap(bitmap)
//            _PhotoView.isVisible = true
//            uploadImage(bitmap)
//        }
//        else{
//            displayMessage(baseContext, "Request cancelled, Va fa Napoli!")
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun convertImage(bitmap: Bitmap) : String {
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val image = stream.toByteArray()
////        return Base64.getEncoder().encodeToString(image)
//        return Base64.encodeToString(image, Base64.DEFAULT)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun uploadImage(image: Bitmap){
//        val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)
//        val imageString = convertImage(image)
//        service.addImage(Image(imageString)).enqueue(object : Callback<Boolean>{
//            override fun onFailure(call: Call<Boolean>, t: Throwable) {
//                displayMessage(baseContext, t.message.toString())
//                Log.d("error",t.message.toString())
//            }
//
//            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
//                displayMessage(baseContext, "Blana!")
//            }
//        })
//    }
//
//    private fun createImageFile(): File? {
//
//        val mediaStorageDir = File(
//            Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            DIRECTORY_NAME)
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                displayMessage(baseContext, "Unable to create directory.")
//                return null
//            }
//        }
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
//            Locale.getDefault()).format(Date())
//
//        return File(mediaStorageDir.path + File.separator
//                + "IMG_" + timeStamp + ".jpg")
//
////        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
////        val imageFileName = "JPEG_$timeStamp" + "_"
//////        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
////        val image = File.createTempFile(
////            imageFileName, /* prefix */
////            ".jpg", /* suffix */
////            storageDir      /* directory */
////        )
////
////        currentPhotoPath = image.absolutePath
////        return image
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        if (requestCode == 0) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                captureImage()
//            }
//        }
//
//    }
//

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}



