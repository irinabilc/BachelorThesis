package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import irinabilc.bachelorthesis.image.CameraActivity
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginUserActivity"

    private lateinit var binding: LoginActivityBinding

    //Firebase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterAccountActivity::class.java))
        }


    }

    private fun loginUser() {
        if (validateFields()) {

            mAuth!!.signInWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEMailAndPassword:success")
                        updateUI()
                    } else {
                        Log.e(TAG, "signInWithEMailAndPassword:failure", task.exception)
                        Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUI() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    private fun validateFields(): Boolean {
        binding.emailEditText.validate({ s -> s.isValid() }, "Invalid username!")
        binding.passwordEditText.validate({ s -> s.isValid() }, "Invalid password!")
        if (binding.emailEditText.error != null || binding.passwordEditText.error != null)
            return false
        return true
    }
}
