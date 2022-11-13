package uz.eloving.farmy.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.FragmentItemShopBinding

class ItemShopFragment : Fragment() {
    private lateinit var binding: FragmentItemShopBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_shop, container, false)
    }
}