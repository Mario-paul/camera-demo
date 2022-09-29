package com.example.camerademo

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
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
import com.example.camerademo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
    private val requestPermissionAndLaunchCamera: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // If permission granted, go to camera with an intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(intent)
            } else {
                // If permission is denied, show text
                Toast.makeText(this, "Error: permission for camera denied by user", Toast.LENGTH_LONG)
                    .show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCamera.setOnClickListener {
            checkForPermissionAndLaunchCamera()
        }

    }


    private fun checkForPermissionAndLaunchCamera() {
        // If user denied permission earlier, show rationale dialog
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            showRationaleDialog(
                "Permissions error",
                "You need to allow access to camera to use this feature."
            )
        } else {
            // If user hasn't responded yet, request permission for camera
            requestPermissionAndLaunchCamera.launch(Manifest.permission.CAMERA)
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

    fun test(){

    }

}