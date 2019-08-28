package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import irinabilc.bachelorthesis.camera_feature.CameraActivity
import irinabilc.bachelorthesis.domain.User
import irinabilc.bachelorthesis.networking.ApiNetworkInterface
import irinabilc.bachelorthesis.networking.ServiceGenerator
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterAccountActivity : AppCompatActivity() {
    // UI elements
    private var _emailText: EditText? = null
    private var _passwordText: EditText? = null
    private var _confirmPassword: EditText? = null
    private var _registerButton: Button? = null
    private var _progressBar: ProgressBar? = null
    private var _firstNameText: EditText? = null
    private var _lastNameText: EditText? = null

    // Firebase elements
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    //Logging
    private val TAG = "CreateAccountActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<RegisterActivityBinding>(this, R.layout.activity_register_account)

        _emailText = binding.emailEditText
        _passwordText = binding.passwordEditText
        _firstNameText = binding.firstNameEditText
        _lastNameText = binding.lastNameEditText
        _confirmPassword = binding.confirmPasswordEditText
        _registerButton = binding.registerButton
        _progressBar = ProgressBar(this)
        _progressBar!!.isVisible = false


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
            _registerButton!!.isEnabled = true
            return
        }

        _registerButton!!.isEnabled = false

        _progressBar!!.isVisible = true

        mAuth!!.createUserWithEmailAndPassword(_emailText!!.text.toString(), _passwordText!!.text.toString())
            .addOnCompleteListener { task ->
                _progressBar!!.isVisible = false

                if (task.isSuccessful) {

                    Log.d(TAG, "createUserWithEmailAndPassword:success")

                    val userid = mAuth!!.currentUser!!.uid

                    verifyEmail()

                    val currentUserDB = mDatabaseReference!!.child(userid)
                    currentUserDB.child("firstName").setValue(_firstNameText!!.text.toString())
                    currentUserDB.child("lastName").setValue(_lastNameText!!.text.toString())

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
            }
            else{
                Log.e(TAG,"sendEmailVerification:failure", task.exception)
                Toast.makeText(this,"Email verification failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register() {
        if (!validateFields()) {
            Toast.makeText(this@RegisterAccountActivity, "Invalid data!", Toast.LENGTH_SHORT).show()
            _registerButton!!.isEnabled = true
            return
        }

        _registerButton!!.isEnabled = false

        val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)

        service.addUser(User(_emailText?.text.toString(), _passwordText?.text.toString()))
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@RegisterAccountActivity, "Register error!", Toast.LENGTH_SHORT).show()
                    _registerButton!!.isEnabled = true

                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Toast.makeText(this@RegisterAccountActivity, "Register successful!", Toast.LENGTH_SHORT).show()
                    _registerButton!!.isEnabled = true
                    startActivity(Intent(this@RegisterAccountActivity, LoginActivity::class.java))
                    finish()
                }

            }
            )
    }


    private fun validateFields(): Boolean {
        _emailText?.validate({ s -> s.isValid() }, "Invalid username!")
        _passwordText?.validate({ s -> s.isValid() }, "Invalid password!")
        _confirmPassword?.validate({ s -> s.isValid() }, "Invalid password!")
        _confirmPassword?.validate(
            { s -> s.equals(_passwordText?.text.toString()) },
            "Passwords don't coincide!"
        )

        if (_emailText?.error != null || _passwordText?.error != null ||
            _confirmPassword?.error != null
        )
            return false
        return true
    }
}
