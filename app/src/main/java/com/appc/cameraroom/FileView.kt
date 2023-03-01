package com.appc.cameraroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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