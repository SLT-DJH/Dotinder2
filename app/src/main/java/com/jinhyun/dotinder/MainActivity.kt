package com.jinhyun.dotinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var al = ArrayList<String>()
    var arrayAdapter: ArrayAdapter<String>? = null
    var i: Int = 0

    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        al.add("php")
        al.add("c")
        al.add("python")
        al.add("java")
        al.add("html")
        al.add("c++")
        al.add("css")
        al.add("javascript")

        arrayAdapter = ArrayAdapter(this, R.layout.item, R.id.helloText, al)

        val flingContainer : SwipeFlingAdapterView = findViewById(R.id.frame)

        flingContainer.adapter = arrayAdapter
        flingContainer.setFlingListener(object  : SwipeFlingAdapterView.onFlingListener{
            override fun removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!")
                al.removeAt(0)
                arrayAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                Toast.makeText(applicationContext, R.string.dislike, Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(p0: Any?) {
                Toast.makeText(applicationContext, R.string.like,Toast.LENGTH_SHORT).show()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                val a = "XML "
                val b = i.toString()
                al.add(a+b)
                arrayAdapter?.notifyDataSetChanged()
                Log.d("LIST", "notified")
                i++

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
}
