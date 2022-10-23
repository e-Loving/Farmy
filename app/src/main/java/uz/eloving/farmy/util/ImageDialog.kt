package uz.eloving.farmy.util

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import uz.eloving.farmy.R

class ImageDialog : DialogFragment(R.layout.fragment_dialog_image) {


    override fun onStart() {
        super.onStart()
        isCancelable = true
    }

}
