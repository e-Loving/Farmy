package uz.eloving.farmy.util

import android.content.Intent
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.eloving.farmy.ui.welcome.WelcomeActivity

class Utils {
    companion object {
        fun String.asToast(activity: FragmentActivity) =
            Toast.makeText(activity, this, Toast.LENGTH_SHORT).show()

        fun onBackPressedListener(
            activity: FragmentActivity,
            container: Int? = null,
            to: Fragment? = null,
            action: Boolean = false
        ) {
            if (action) activity.supportFragmentManager.beginTransaction()
                .replace(container!!, to!!).commit()
            else if (container == null || to == null)
                activity.onBackPressedDispatcher
                    .addCallback(activity, object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            activity.finishAffinity()
                        }
                    })
            else activity.onBackPressedDispatcher
                .addCallback(activity, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        Intent(activity, WelcomeActivity::class.java).let {
                            activity.supportFragmentManager.beginTransaction()
                                .replace(container, to).commit()
                        }
                    }
                })
        }
    }
}