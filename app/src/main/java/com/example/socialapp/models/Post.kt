package com.example.socialapp.models

data class Post(
    val text:String="",
    val createdby:User=User(),
    val createdAt:Long=0L,
    val likedby:ArrayList<String> =ArrayList()
)