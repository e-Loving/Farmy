package uz.eloving.vcraft.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.eloving.vcraft.data.MockData

class WelcomeFragmentAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return WelcomeFragment(MockData.data[position])
    }

}