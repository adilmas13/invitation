package com.invitation


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.invitation.models.ContactModel
import com.invitation.models.LocationModel
import com.mehta.akshaywedding.R
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.view_contacts.view.*
import kotlinx.android.synthetic.main.view_location_tabs.*
import java.io.IOException


class LocationFragment : Fragment() {

    private lateinit var locationItems: List<LocationModel>

    private lateinit var contacts: List<ContactModel>

    private var previousSelectedTab: LinearLayout? = null

    private var selectedTabPosition = 0

    private val tabClickListener = View.OnClickListener {
        val index = llTabParent.indexOfChild(it)
        selectedTabPosition = index
        onTabSelected(index)

        (it as LinearLayout).apply {
            getChildAt(2).visibility = View.VISIBLE
            alpha = 1f
        }

        previousSelectedTab?.let {
            it.apply {
                getChildAt(2).visibility = View.INVISIBLE
                alpha = 0.3f
            }
        }

        previousSelectedTab = it
    }

    private var callListener = View.OnClickListener {
        redirectToCall(contacts[llContacts.indexOfChild(it.parent as View)].number)
    }


    private fun onTabSelected(p2: Int) {
        tcName.text = locationItems[p2].name
        tvAddress.text = locationItems[p2].address
        /*when (p2) {
            0 -> ivLogo.setImageResource(R.drawable.ic_home_black_24dp)
            1 -> ivLogo.setImageResource(R.drawable.icons_city_church)
            2 -> ivLogo.setImageResource(R.drawable.ic_sangeet)
            3 -> ivLogo.setImageResource(R.drawable.ic_marriage)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setup()
    }

    private fun setup() {
        setupContacts()
        setupTabs()
        direction.setOnClickListener {
            val data = locationItems[selectedTabPosition]
            redirectToMap(data.lat, data.lng, data.title)
        }
    }

    private fun setupTabs() {
        val data = getAssetJsonData(context!!, "location.json")
        val type = object : TypeToken<List<LocationModel>>() {}.type
        locationItems = Gson().fromJson(data, type)
        previousSelectedTab = llHome
        llHome.setOnClickListener(tabClickListener)
        llMandva.setOnClickListener(tabClickListener)
        llSangeet.setOnClickListener(tabClickListener)
        llMarriage.setOnClickListener(tabClickListener)
        onTabSelected(selectedTabPosition)
    }

    private fun setupContacts() {
        val data = getAssetJsonData(context!!, "contacts.json")
        val type = object : TypeToken<List<ContactModel>>() {}.type
        contacts = Gson().fromJson(data, type)
        contacts.forEach {
            val view = LayoutInflater.from(context!!).inflate(R.layout.view_contacts, null)
            view.tvContactName.text = it.name
            view.tvContactNo.text = it.number
            view.ivCall.setOnClickListener(callListener)
            view.ivContact.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    getDrawable(it.image)
                )
            )
            llContacts.addView(view)
        }
    }

    fun getDrawable(image: String): Int {
        return when (image) {
            "shital" -> R.drawable.shital
            "kaushal" -> R.drawable.kaushal
            "kalpesh" -> R.drawable.kalpesh
            else -> 0
        }
    }


    private fun redirectToCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun redirectToMap(lat: Double, lng: Double, name: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$lng ($name)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        activity?.packageManager?.let {
            if (mapIntent.resolveActivity(it) != null) {
                startActivity(mapIntent)
            }
        }
    }

    private fun getAssetJsonData(context: Context, fileName: String): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(fileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}
