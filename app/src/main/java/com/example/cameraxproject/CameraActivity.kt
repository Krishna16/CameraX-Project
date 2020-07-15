package com.example.cameraxproject

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.System.DATE_FORMAT
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CameraActivity : AppCompatActivity() {

    private var lensFacing = CameraX.LensFacing.BACK
    private val TAG = "CameraActivity"
    private val permissionArray: Array<String> = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val CAMERA_STORAGE_CODE = 1000
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        firebaseStorage = FirebaseStorage.getInstance()

        gallery.setOnClickListener {
            var intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
            //finish()
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_STORAGE_CODE)
        }

        texture.post{
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Please Grant Permissions First", Toast.LENGTH_SHORT).show()
            }
            else
            startCamera()
        }

        texture.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            updateTransform()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_STORAGE_CODE){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
                startCamera()
            }
            else{
                Toast.makeText(this, "Please Grant Permissions First", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val metrics = DisplayMetrics().also { texture.display.getRealMetrics(it) }
        val screenSize = Size(metrics.widthPixels, metrics.heightPixels)
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetResolution(screenSize)
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(windowManager.defaultDisplay.rotation)
            setTargetRotation(texture.display.rotation)
        }.build()

        val preview = Preview(previewConfig)
        preview.setOnPreviewOutputUpdateListener {
            texture.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // Create configuration object for the image capture use case
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setLensFacing(lensFacing)
                setTargetAspectRatio(screenAspectRatio)
                setTargetRotation(texture.display.rotation)
                setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            }.build()

        // Build the image capture use case and attach button click listener
        val imageCapture = ImageCapture(imageCaptureConfig)
        btn_take_picture.setOnClickListener {

            val file = File(
                getExternalFilesDir("camera").toString() +
                        "${System.currentTimeMillis()}.jpg"
            )

            imageCapture.takePicture(file, object : ImageCapture.OnImageSavedListener {
                    override fun onError(
                        error: ImageCapture.UseCaseError,
                        message: String, exc: Throwable?
                    ) {
                        val msg = "Photo capture failed: $message"
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun onImageSaved(file: File) {
                        Toast.makeText(this@CameraActivity, "Uploading Image To Firebase....", Toast.LENGTH_LONG).show()
                        val storage = FirebaseStorage.getInstance("gs://camerax-project.appspot.com")
                        var storageRef = storage.reference
                        val imageRef = storageRef.child("images/" + file.name)
                        var uploadTask = imageRef.putFile(Uri.fromFile(file))
                        uploadTask.addOnFailureListener{
                            Toast.makeText(this@CameraActivity, "Image Upload Failed: Do you have internet access?", Toast.LENGTH_LONG).show()
                        }.addOnCompleteListener { it1 ->
                            Toast.makeText(this@CameraActivity, "Image Upload Succeeded!!", Toast.LENGTH_LONG).show()
                            if (it1.isSuccessful) {
                                databaseReference = Firebase.database.reference
                                Log.d(TAG, "" + databaseReference.database)
                                imageRef.downloadUrl.addOnSuccessListener {
                                    Log.d(TAG, "it value: " + it)
                                    databaseReference.child("images")
                                        .child(file.name.substring(0, file.name.lastIndexOf(".")))
                                        .setValue(it.toString())
                                }
                            }
                        }
                    }
                })
        }

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun updateTransform() {
        val matrix = Matrix()
        val centerX = texture.width / 2f
        val centerY = texture.height / 2f

        val rotationDegrees = when (texture.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        texture.setTransform(matrix)
    }

    companion object{
        private val folderPath: String = ""
    }
}