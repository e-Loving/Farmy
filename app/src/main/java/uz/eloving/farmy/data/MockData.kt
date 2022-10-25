package uz.eloving.farmy.data

import uz.eloving.farmy.R
import uz.eloving.farmy.model.UIModule

class MockData {
    companion object {
        val data = arrayListOf(
            UIModule(
                "O'simligingiz kasalligini aniqlay olmayapsizmi ? ", R.raw.save_nature,
                "Bizga o'simlik rasmini yuklang, biz esa sizga undagi kasallikni " +
                        "aniqlashga yordam beramiz !",
                false
            ),
            UIModule(
                "Sotish va sotib olishda shaffoflik qidiryapsizmi ?", R.raw.sell_and_buy,
                "Biz sizga mevalardan tortib poliz ekinlarigacha oson sotish va sotib " +
                        "olishni taklif qilamiz !",
                false
            ),
            UIModule(
                "Ish yoki ishchi qidirishda muammo bormi ? ", R.raw.search_for_job,
                "Biz orqali o'zingizga oson ish va ishchi toping. Ishchilar va ish " +
                        "beruvchilar uchun eng afzal yechim — Farmy",
                true
            )
        )
    }
}