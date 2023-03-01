package com.appc.cameraroom

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var img: ImageView
    private var permissions = arrayOf(Manifest.permission.CAMERA)
    private var CAMERA_PERMISSION_CODE = 1
    private var CAMERA_REQUEST_CODE = 2
    private lateinit var vib : Vibrator
    private val VIB_RATE : Long = 22

    private val imageLocationViewModel : ImageLocationViewModel by viewModels {
        TaskItemModelFactory((application as ImageLocationApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        vib = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        img = findViewById(R.id.image)
        fab.setOnClickListener {
            vib.vibrate(VIB_RATE)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE)
            }
        }
        val adapter = ImageLocationAdapter()
        imageLocationViewModel.imageLocation.observe(this, androidx.lifecycle.Observer {
            it.let {
                adapter.submitList(it)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this, "Sorry!, Permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                img.setImageBitmap(data!!.extras!!["data"] as Bitmap?)
                saveBitmap(data.extras!!["data"] as Bitmap?)
            }
        }
    }

    private fun saveBitmap(bitmap: Bitmap?) {
        val root = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/CamAPI"
        val myDir = File(root)
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val file = File(myDir, "IMG_${UUID.randomUUID()}.png")
        if (file.exists()) {
            file.delete()
        }
        pushImageLocationToRoom(file.absolutePath)
        try {
            val out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun pushImageLocationToRoom(fileLocation : String) {
        /*val f =FilenameRepository(this)
        GlobalScope.launch(Dispatchers.IO) {
            f.insert(fileLocation)
            val items = f.getAllItems()
            for(str in items) {
                Log.d("image location", str.location)
            }
        }*/
        val location = ImageLocation(location = fileLocation)
        imageLocationViewModel.addImageLocationItem(location)
    }

    fun fetchItem(view: View) {
        vib.vibrate(VIB_RATE)
        val intent = Intent(this, FileView::class.java)
        startActivity(intent)
    }
}