package uz.eloving.farmy.util

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import uz.eloving.farmy.R

class ProgressDialog : DialogFragment(R.layout.dialog_progress) {

    override fun onStart() {
        super.onStart()
        isCancelable = false
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

}

fun DialogFragment.show(fragmentManager: FragmentManager) {
    if (!this.isVisible)
        this.show(fragmentManager, "progress_dialog")
}

fun DialogFragment.hide() {
    if (this.isVisible)
        this.dismiss()
}
