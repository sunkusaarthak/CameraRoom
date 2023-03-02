package com.appc.cameraroom

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileView : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_view)
        supportActionBar!!.hide()
        recyclerView = findViewById(R.id.recyclerView)
        val adapter = ImageLocationAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.onItemClick = {
            val file = File(it.location)
            val uri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", file)
            val shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.setDataAndType(uri, "image/*")
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(shareIntent, "Open Image"))
        }

        imageLocationViewModel1.imageLocation.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })
    }

    private val imageLocationViewModel1 : ImageLocationViewModel by viewModels {
        TaskItemModelFactory((application as ImageLocationApplication).repository)
    }
}