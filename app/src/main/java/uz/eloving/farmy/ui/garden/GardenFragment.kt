package uz.eloving.farmy.ui.garden

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        db.gardenDao().getItem().observe(viewLifecycleOwner) {
            this.list = it
            adapterGarden.updateList(list)
        }
    }

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
}