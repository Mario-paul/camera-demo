package com.example.camerademo

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camerademo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    companion object {
//        private const val CAMERA_PERMISSION_CODE = 1
//        private const val CAMERA_REQUEST_CODE = 2
//    }

    // Create launcher for camera
    private val cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val cameraImage: Bitmap = result.data?.extras?.get("data") as Bitmap
            binding.imageBox.setImageBitmap(cameraImage)
        }
    }

    // Create ActivityResultLauncher for granting permission
    private val requestPermission: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // If permission granted, go to camera with an intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(intent)
            } else {
                // If permission is denied, show text
                Toast.makeText(this, "Permission denied by user.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCamera.setOnClickListener {

//            if (ContextCompat.checkSelfPermission(
//                    this, Manifest.permission.CAMERA
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(intent, CAMERA_REQUEST_CODE)
//            } else {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
//                )
//            }

            requestCameraPermission()

        }

    }

    private fun requestCameraPermission() {
        // If user denied permission earlier, show rationale dialog
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            showRationaleDialog(
                "Camera Demo",
                "You need to allow access to camera to use this feature."
            )
        } else {
            // If user hasn't responded yet, request permission for camera
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("SETTINGS") { _, _ ->

            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }

        }.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        if (binding != null) {
//            binding = null
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_PERMISSION_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(intent, CAMERA_REQUEST_CODE)
//            } else {
//                Toast.makeText(
//                    this,
//                    "Permission denied for camera. You can grant it by going to system settings.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST_CODE) {
//                val cameraPicture: Bitmap = data?.extras?.get("data") as Bitmap
//                binding.imageBox.setImageBitmap(cameraPicture)
//            }
//        }
//    }

}