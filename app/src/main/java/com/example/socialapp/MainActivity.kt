package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.socialapp.Daos.UserDao
import com.example.socialapp.databinding.ActivityMainBinding
import com.example.socialapp.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private companion object
    {
        private const val RC_SIGN_IN=100
        private const val TAG="GOOGLE_SIGN_IN_TAG"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG,"onActivityResult: Google Signin Result")
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("326935296038-v7m6f79o18p9nns4a3vgbshudhuaja0h.apps.googleusercontent.com")
            .requestEmail()
            .build()

        Log.d(TAG,"onActivityResult: Google Signin Result")
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth= FirebaseAuth.getInstance()

        Log.d(TAG,"onActivityResult: Google Signin Result")
        binding.signinButton.setOnClickListener{

            Log.d(TAG,"onActivityResult: Google Signin Result")
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }
    }

    override fun onStart() {
        super.onStart()
        val currentuser=firebaseAuth.currentUser

        Log.d(TAG,"onActivityResult: Google Signin Result00000000")
        updateUI(currentuser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG,"onActivityResult: Google Signin Result")
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG,"onActivityResult: Google Signin Result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed "+e.message)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG,"firebaseAuth with google Account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        binding.signinButton.visibility=View.GONE
        binding.progressbar.visibility= View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth=firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser=auth.user
            withContext(Dispatchers.Main)
            {
                updateUI(firebaseUser)
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
         if(firebaseUser!=null)
         {
             val user=User(firebaseUser.uid,firebaseUser.displayName,firebaseUser.photoUrl.toString())
             val usersDao=UserDao()
             usersDao.addUser(user)

             Log.d(TAG,"onActivityResult: Google Signin Result11111111111")
             val intent=Intent(this,ProfileActivity::class.java)
             startActivity(intent)
             finish()
         }
        else
         {
             binding.signinButton.visibility=View.VISIBLE
             binding.progressbar.visibility= View.GONE
         }
    }

}