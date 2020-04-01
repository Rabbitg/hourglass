package com.timeattack.hourglass.Login

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.timeattack.hourglass.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        //회원가입 버튼
        registerButton.setOnClickListener {
            //회원 가입 입력 폼
            performRegister()
        }
        selectPhoto_button_register.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }


        // 뒤로가기 버튼
        backbuttonRegister.setOnClickListener {
            super.onBackPressed()
        }

    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectPhoto_imageview_register.setImageBitmap(bitmap)
            selectPhoto_button_register.alpha = 0f
        }
    }


    private fun uploadImageToFirebaseStorage() {

        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")
                    saveUserToFirebaseDataBase(it.toString())
                }
            }
            .addOnFailureListener {
                // do some logging here
            }
    }

    private fun saveUserToFirebaseDataBase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,nameRegisterEtv.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
            }
    }

    // 회원가입 함수
    private fun performRegister(){
        // 회원가입 나열되어 있는 것
        val name = findViewById<EditText>(R.id.nameRegisterEtv)
        val email = emailRegisterEtv.text.toString()
        val nickname = findViewById<EditText>(R.id.nickNameRegisterEtv)
        val password = pwRegisterEtv.text.toString()
        val passwordCheck = findViewById<EditText>(R.id.pwCheckRegisterEtv)

        if(email.isEmpty() || password.isEmpty() ||
            name.text.toString().isEmpty() || nickname.text.toString().isEmpty() || passwordCheck.text.toString().isEmpty())
        {
            Toast.makeText(this, "번거로우시겠지만 모두 작성해주세요 ^^", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6)
        {
            Toast.makeText(this, "비밀번호는 7자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else if (password != passwordCheck.text.toString())
        {
            Toast.makeText(this, "두 비밀번호가 일치하여야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    if (task.isSuccessful){
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        finish()
                    }else
                    {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        }

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if successful
                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
    }

    class User(val uid:String, val username:String, val profileImageUrl:String)

}
