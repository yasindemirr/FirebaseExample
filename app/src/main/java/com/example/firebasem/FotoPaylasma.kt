package com.example.firebasem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebasem.databinding.ActivityFotoPaylasmaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp
import java.util.UUID

class FotoPaylasma : AppCompatActivity() {
    private lateinit var storage:FirebaseStorage
    private lateinit var database:FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityFotoPaylasmaBinding
    var bitmapim:Bitmap?=null
    var sectigimGorsel:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFotoPaylasmaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage= FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()



        }

    fun gorselSec(view: View){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else   {
            val galeriyeGecis = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriyeGecis,2)
        }

    }
    fun gorselPaylas(view: View){
        val uuid= UUID.randomUUID()
        val gorselAdi="${uuid}.jpg"
        val referans= storage.reference
        val gorselReferans= referans.child("images").child(gorselAdi)
        if (sectigimGorsel!=null){
            gorselReferans.putFile(sectigimGorsel!!).addOnSuccessListener{
                val yuklenenGorselReferansı=FirebaseStorage.getInstance().reference.child("images").child(gorselAdi)
               yuklenenGorselReferansı.downloadUrl.addOnSuccessListener {
                   val inenUri= it.toString()
                   val kullaniciMaili= auth.currentUser!!.email.toString()
                   val kullaniciTarih=com.google.firebase.Timestamp.now()
                   val kullaniciYorumu= binding.editFotoPaylasma.text.toString()
                   val postHashMap= hashMapOf<String,Any>()
                   postHashMap.put("gorselUri",inenUri)
                   postHashMap.put("kullanıcıMail",kullaniciMaili)
                   postHashMap.put("kullaniciYorum",kullaniciYorumu)
                   postHashMap.put("tarih",kullaniciTarih)
                   database.collection("post").add(postHashMap).addOnCompleteListener {
                       if(it.isSuccessful){
                           finish()
                       }
                   }.addOnFailureListener{
                       Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                   }
               }.addOnFailureListener {
                   Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
               }

            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                val galeriyeGecis = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriyeGecis,2)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2 && resultCode==Activity.RESULT_OK && data!= null){
            sectigimGorsel= data.data
            if(sectigimGorsel!=null){
                if (Build.VERSION.SDK_INT >= 28 ){
                    val kaynak= ImageDecoder.createSource(this.contentResolver,sectigimGorsel!!)
                    bitmapim= ImageDecoder.decodeBitmap(kaynak)
                    binding.imageView.setImageBitmap(bitmapim)

                }else{
                    bitmapim=MediaStore.Images.Media.getBitmap(this.contentResolver,sectigimGorsel)
                    binding.imageView.setImageBitmap(bitmapim)
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}