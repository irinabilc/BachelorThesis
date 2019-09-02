package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import irinabilc.bachelorthesis.image.CameraActivity
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate

class RegisterAccountActivity : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    // Firebase elements
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    //Logging
    private val TAG = "CreateAccountActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_account)



        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            //register()
            createAccount()
        }

        binding.loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun createAccount() {
        if (!validateFields()) {
            Toast.makeText(this@RegisterAccountActivity, "Invalid data!", Toast.LENGTH_SHORT).show()
            binding.registerButton.isEnabled = true
            return
        }

        binding.registerButton.isEnabled = false


        mAuth!!.createUserWithEmailAndPassword(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Log.d(TAG, "createUserWithEmailAndPassword:success")

                    val userid = mAuth!!.currentUser!!.uid

                    verifyEmail()

                    val currentUserDB = mDatabaseReference!!.child(userid)
                    currentUserDB.child("firstName").setValue(binding.firstNameEditText.text.toString())
                    currentUserDB.child("lastName").setValue(binding.lastNameEditText.text.toString())
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    updateUserandUI()


                } else {
                    Log.e(TAG, "createUserWIthEmailAndPassword:failure", task.exception)
                    Toast.makeText(this, "Registration error!", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun updateUserandUI() {
        val intent = Intent(this, CameraActivity::class.java)
        //FLAG_ACTIVITY_CLEAR_TOP clears activity from stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Confirmation email sent to" + mUser.email, Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "sendEmailVerification:failure", task.exception)
                Toast.makeText(this, "Email verification failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        binding.emailEditText.validate({ s -> s.isValid() }, "Invalid username!")
        binding.passwordEditText.validate({ s -> s.isValid() }, "Invalid password!")
        binding.confirmPasswordEditText.validate({ s -> s.isValid() }, "Invalid password!")
        binding.confirmPasswordEditText.validate(
            { s -> s.equals(binding.passwordEditText.text.toString()) },
            "Passwords don't coincide!"
        )

        if (binding.emailEditText.error != null || binding.passwordEditText?.error != null ||
            binding.confirmPasswordEditText.error != null
        )
            return false
        return true
    }
}
