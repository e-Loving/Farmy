package uz.eloving.farmy.ui.garden

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.eloving.farmy.adapter.AdapterGarden
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentGardenBinding
import uz.eloving.farmy.db.GardenDatabase
import uz.eloving.farmy.model.GardenItemModel
import uz.eloving.farmy.util.Categorization


class GardenFragment : Fragment() {
    private lateinit var binding: FragmentGardenBinding
    private lateinit var adapterGarden: AdapterGarden
    private lateinit var categorization: Categorization
    private var list = listOf<GardenItemModel>()
    private val db by lazy {
        GardenDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGardenBinding.inflate(inflater, container, false)
        adapterGarden = AdapterGarden()
        binding.rvGarden.adapter = adapterGarden
        binding.rvGarden.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                updateBySearch(s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateBySearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                updateBySearch(s.toString())
            }
        })
        binding.fab.setOnClickListener {
            resultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        categorization =
            Categorization(
                requireContext().assets,
                "plant_disease_model.tflite",
                "plant_labels.txt",
                224
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateByAll()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                GlobalScope.launch {
                    val res = data!!.extras?.get("data") as Bitmap
                    convertToBitmap(res)
                }
            }
        }

    @OptIn(DelicateCoroutinesApi::class)
    private fun convertToBitmap(bitmap: Bitmap) {
        categorization.recognizeImages(bitmap).let {
            if (it.isNotEmpty()) {
                val results = categorization.recognizeImages(bitmap).first()
                GlobalScope.launch {
                    db.gardenDao()
                        .insertItem(GardenItemModel(bitmap, results.title, results.confidence))
                    list = list + GardenItemModel(bitmap, results.title, results.confidence)
                }
            }
        }
    }

    private fun updateBySearch(text: String) {
        if (text != "") {
            db.gardenDao().getSearchedItems("%${text}%").observe(viewLifecycleOwner) {
                this.list = it
                adapterGarden.updateList(it as ArrayList<GardenItemModel>)
            }
        } else {
            updateByAll()
        }
    }

    private fun updateByAll() {
        db.gardenDao().getItem().observe(viewLifecycleOwner) {
            this.list = it
            adapterGarden.updateList(list as ArrayList<GardenItemModel>)
        }
    }
}