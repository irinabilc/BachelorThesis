package irinabilc.bachelorthesis.image

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.view.TextureView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import irinabilc.bachelorthesis.CameraActivityBinding
import irinabilc.bachelorthesis.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class CameraActivity : AppCompatActivity() {
    private lateinit var _cameraViewFinder: TextureView
    private lateinit var _button: FloatingActionButton

    private val TAG = "CameraActivity"

    val REQUEST_IMAGE_CAPTURE = 1


    private val PERMISSION_REQUEST_CODE: Int = 101

    // This is an array of all the permission specified in the manifest
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    private var photoFile: File? = null
    private lateinit var currentPhotoPath: String
    private val DIRECTORY_NAME = "binOWLculars"
    private var currentBitmap: Bitmap? = null

    private lateinit var binding: CameraActivityBinding


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<CameraActivityBinding>(this, R.layout.activity_camera)
        //setActionBar(binding.myToolbar)
        if (checkPersmission()) takePicture() else requestPermission()


    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA, WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }



    private fun takePicture() {

        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "irinabilc.bachelorthesis.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//
//            //To get the File for further usage
//            val auxFile = File(currentPhotoPath)
//
//
//            currentBitmap = BitmapFactory.decodeFile(currentPhotoPath)
//            binding.imageView.setPic()

            val intent = ProcessImageActivity.getStartIntent(this, currentPhotoPath)
            startActivity(intent)

        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            DIRECTORY_NAME
        )
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(baseContext, "Unable to create directory.")
                throw IOException("Unable to create directory.")
            }
        }

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            mediaStorageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                    takePicture()

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}



