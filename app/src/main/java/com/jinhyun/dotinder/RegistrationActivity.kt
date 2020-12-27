package com.jinhyun.dotinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    val db = FirebaseFirestore.getInstance()

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
            val name = et_name.text.toString()
            val selectGender = rg_gender.checkedRadioButtonId
            val rbtn : RadioButton = findViewById(selectGender)

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty()){
                Toast.makeText(this, R.string.request_name, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rbtn.text == null){
                Toast.makeText(this, R.string.request_gender, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(!it.isSuccessful){
                    return@addOnCompleteListener
                }else{
                    val userId = it.result?.user?.uid.toString()
                    val userinfo = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "password" to password,
                        "uid" to userId,
                        "gender" to rbtn.text.toString()
                    )
                    db.collection(rbtn.text.toString()).document(userId).set(userinfo).addOnSuccessListener {
                        Toast.makeText(this, R.string.register_succeed, Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show()
                    }
                    Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")
                }
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