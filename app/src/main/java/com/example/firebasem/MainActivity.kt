package com.example.firebasem

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebasem.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        val guncelKullanici= auth.currentUser
        if (guncelKullanici!=null){
            val git =Intent(applicationContext,AnaAktivitem::class.java)
            startActivity(git)
            finish()
        }


    }

    fun girisYapAksiyon(view: View) {
        val mailAL= binding.kullaniciAdText.text.toString()
        val sifreAl= binding.kullaniciSifreText.text.toString()
        auth.signInWithEmailAndPassword(mailAL,sifreAl).addOnCompleteListener {
            if (it.isSuccessful){
                val guncelKullanici= auth.currentUser?.email.toString()
                Toast.makeText(applicationContext,"Hosgldiniz :${guncelKullanici}",Toast.LENGTH_LONG).show()
             val diger= Intent(applicationContext,AnaAktivitem::class.java)
             startActivity(diger)
             finish()
            }
        }.addOnFailureListener(){
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    fun kayitOlAksiyon(view: View) {
        val mailAl = binding.kullaniciAdText.text.toString()
        val sifreAl = binding.kullaniciSifreText.text.toString()
        auth.createUserWithEmailAndPassword(mailAl, sifreAl).addOnCompleteListener {
            if (it.isSuccessful) {
                val digerSayfayaGec = Intent(applicationContext, AnaAktivitem::class.java)
                startActivity(digerSayfayaGec)
                finish()


            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}