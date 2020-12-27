package com.jinhyun.dotinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    var cards = ArrayList<Cards>()
    val itemAdapter = ItemAdapter(cards)

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkUserGender()

        val flingContainer : SwipeFlingAdapterView = findViewById(R.id.frame)

        flingContainer.adapter = itemAdapter
        flingContainer.setFlingListener(object  : SwipeFlingAdapterView.onFlingListener{
            override fun removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!")
                cards.removeAt(0)
                itemAdapter.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                Toast.makeText(applicationContext, R.string.dislike, Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(p0: Any?) {
                Toast.makeText(applicationContext, R.string.like,Toast.LENGTH_SHORT).show()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {

            }

            override fun onScroll(p0: Float) {

            }
        })

        flingContainer.setOnItemClickListener(object : SwipeFlingAdapterView.OnItemClickListener{
            override fun onItemClicked(p0: Int, p1: Any?) {
                Toast.makeText(applicationContext, "Clicked!",Toast.LENGTH_SHORT).show()
            }
        })

        btn_left.setOnClickListener {
            flingContainer.topCardListener.selectLeft()
        }

        btn_right.setOnClickListener {
            flingContainer.topCardListener.selectRight()
        }

        btn_signout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginRegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun checkUserGender(){
        var mgender = ""

        //check if Male
        db.collection("Male").document(mAuth.uid.toString()).get().addOnSuccessListener { it ->
            if (it.data != null){
                mgender = "Male"
                logGender(mgender)
                getOppositeGenderUsers("Female")
            }else{
                mgender = "Female"
                logGender(mgender)
                getOppositeGenderUsers("Male")
            }
        }
    }

    fun logGender(gender : String){
        Log.d(TAG, "log gender : $gender")
    }

    fun getOppositeGenderUsers(oppoisitegender : String){
        db.collection(oppoisitegender).addSnapshotListener { value, error ->
            if (error != null) {
                Log.d(TAG, "getOppositeGenderUsers failed.", error)
                return@addSnapshotListener
            }

            for (doc in value!!){
                cards.add(Cards(doc.getString("uid").toString(), doc.getString("name").toString()))
                itemAdapter.notifyDataSetChanged()
            }
        }
    }
}
