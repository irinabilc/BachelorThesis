package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import irinabilc.bachelorthesis.image.CameraActivity
import irinabilc.bachelorthesis.storage.textentrylist.TextEntryListActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.activity_main)

        checkUser()

        binding.logoutButton.setOnClickListener {
            logoutUser()
        }

        binding.scanPictureButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.savedEntriesButton.setOnClickListener {
            val intent = Intent(this, TextEntryListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun logoutUser() {
        val auth = FirebaseAuth.getInstance()
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun checkUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

    }

}
