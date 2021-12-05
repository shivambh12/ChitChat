package com.example.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.socialapp.Daos.PostDao

class CreatePost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        val posttext:EditText=findViewById(R.id.post)
        val postbutton:Button=findViewById(R.id.postbutton)
        postbutton.setOnClickListener {
            val input=posttext.text.toString().trim()
            Log.d("Hello",input)
            if(input.isNotEmpty())
            {
                val postDao=PostDao()
                postDao.addPost(input)
                Log.d("Hello",input)
                finish()
            }
        }
    }
}