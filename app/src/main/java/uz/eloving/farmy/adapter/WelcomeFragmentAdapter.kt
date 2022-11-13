package uz.eloving.farmy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.eloving.farmy.data.MockData
import uz.eloving.farmy.ui.welcome.ViewPagerFragment

class WelcomeFragmentAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment(MockData.data[position])
    }
}