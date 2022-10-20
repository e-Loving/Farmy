package uz.eloving.farmy.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.FragmentAddBinding

class AddFragment : DialogFragment() {
    private lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

}