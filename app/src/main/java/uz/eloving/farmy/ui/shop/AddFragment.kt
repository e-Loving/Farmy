package uz.eloving.farmy.ui.shop

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.FragmentAddBinding

class AddFragment : DialogFragment() {
    private lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onCancel(dialog: DialogInterface) {
        Toast.makeText(requireContext(), "Amal bekor qilindi", Toast.LENGTH_SHORT).show()
    }

}