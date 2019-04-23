package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import irinabilc.bachelorthesis.domain.User
import irinabilc.bachelorthesis.networking.ApiNetworkInterface
import irinabilc.bachelorthesis.networking.ServiceGenerator
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class RegisterAccountActivity : AppCompatActivity() {
    var _usernameText: EditText? = null
    var _passwordText: EditText? = null
    var _confirmPassword: EditText? = null
    var _registerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<RegisterActivityBinding>(this, R.layout.activity_register_account)

        _usernameText = binding.usernameEditText
        _passwordText = binding.passwordEditText
        _confirmPassword = binding.confirmPasswordEditText
        _registerButton = binding.registerButton

        binding.registerButton.setOnClickListener {
            register()
        }

        binding.loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {
        if (!validateFields()) {
            _registerButton!!.isEnabled = false
            Toast.makeText(this@RegisterAccountActivity, "Invalid data!", Toast.LENGTH_SHORT).show()
            return
        }

        val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)

        service.addUser(User( _usernameText?.text.toString(), _passwordText?.text.toString()))
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
        _usernameText?.validate({ s -> s.isValid() }, "Invalid username!")
        _passwordText?.validate({ s -> s.isValid() }, "Invalid password!")
        _confirmPassword?.validate({ s -> s.isValid() }, "Invalid password!")
        _confirmPassword?.validate(
            { s -> s.equals(_passwordText?.text.toString()) },
            "Passwords don't coincide!"
        )

        if (_usernameText?.error != "" || _passwordText?.error != "" ||
            _confirmPassword?.error != ""
        )
            return false
        return true
    }
}
