package com.jinhyun.dotinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    val TAG = "RegistrationActivity"

    val mAuth = FirebaseAuth.getInstance()
    val authStateListner = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if(firebaseUser != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btn_registration.setOnClickListener {
            val email = et_email_regi.text.toString()
            val password = et_password_regi.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")
            }.addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(this.authStateListner)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(this.authStateListner)
    }

}