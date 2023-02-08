package com.example.firebasem

import android.content.Context
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.firebasem.databinding.ActivityAnaAktivitemBinding
import com.example.firebasem.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class AnaAktivitem : AppCompatActivity() {
private lateinit var auth: FirebaseAuth
private lateinit var dataBase:FirebaseFirestore
private lateinit var binding: ActivityAnaAktivitemBinding
private lateinit var recyclerViewAdepter: HaberlerAdepter

    var postListem= ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAnaAktivitemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        dataBase=FirebaseFirestore.getInstance()
        veriAl()
       var layoutManager=LinearLayoutManager(this)
        binding.RecyclerView.layoutManager=layoutManager

            recyclerViewAdepter= HaberlerAdepter(postListem)
            binding.RecyclerView.adapter=recyclerViewAdepter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.secenek,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId.equals(R.id.cikisYap)){
            auth.signOut()
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else if (item.itemId.equals(R.id.fotografPaylas)){
            val intent= Intent(applicationContext,FotoPaylasma::class.java)
            startActivity(intent)
            


        }
        return super.onOptionsItemSelected(item)
    }
    fun veriAl(){
        dataBase.collection("post").orderBy("tarih",Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
            if (exception!=null){
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else {
                if (snapshot!= null){
                    if (!snapshot.isEmpty){
                        val dokumanlar= snapshot.documents

                        postListem.clear()
                        for (dokuman in dokumanlar){
                            val kullaniciMail=dokuman.get("kullanıcıMail") as String
                            val kullaniciUri= dokuman.get("gorselUri")as    String
                            val kullaniciYorum= dokuman.get("kullaniciYorum")as String
                            val indirilenPost= Post(kullaniciMail,kullaniciYorum,kullaniciUri)
                            postListem.add(indirilenPost)





                        }
                        recyclerViewAdepter.notifyDataSetChanged()




                    }
                }

            }
        }
        }
    }
