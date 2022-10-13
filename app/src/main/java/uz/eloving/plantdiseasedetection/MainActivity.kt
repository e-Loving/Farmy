package uz.eloving.plantdiseasedetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mCategorization: Categorization
    private lateinit var mBitmap: Bitmap
    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2
    private val inputSize = 224
    private val mModelPath = "plant_disease_model.tfLite"
    private val mLabelPath = "plant_labels.txt"

    private val mSamplePath = "automn.jpg"
    lateinit var toolbar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView = findViewById<View>(R.id.nav_views) as NavigationView
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.setViewScale(GravityCompat.START, 0.9f)


        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, 0, 0
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        drawer.setViewElevation(GravityCompat.START, Color.TRANSPARENT)
        drawer.drawerElevation(GravityCompat.START, 20f)
        drawer.setContrastThreshold(3f)
        drawer.setRadious(GravityCompat.START, 25f)
        drawer.useCustomBehaviour(GravityCompat.START)
        drawer.useCustomBehaviour(GravityCompat.END)
        val headerview = navigationView.getHeaderView(0)

        mCategorization = Categorization(assets, mModelPath, mLabelPath, inputSize)


        resources.assets.open(mSamplePath).use {
            mBitmap=BitmapFactory.decodeStream(it)
            mBitmap=Bitmap.createScaledBitmap(mBitmap,inputSize,inputSize,TR)
         mPhotoImageView.setImageBitmap(mBitmap)



        }
val bottomNav:SmoothBottombar=findViewById<SmoothBottomBar>(R.id.bottom_nav)

    }
}