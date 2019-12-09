package com.example.watcher.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watcher.R
import com.example.watcher.adapters.RecyclerAdapter
import com.example.watcher.helpers.LocationService
import com.example.watcher.helpers.SMS
import com.example.watcher.model.Broadcast
import com.example.watcher.types.BroadcastListType
import com.example.watcher.types.BroadcastType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.broadcast_item_row.view.*


class BroadcastFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: RecyclerAdapter<BroadcastType>? = null
    private val messageViewId = View.generateViewId()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_broadcast, container, false)
        showBroadCastList(view)
        addEventListeners(view)

        return view
    }

    private fun showBroadCastList(view: View): RecyclerView? {
        val broadCasts = getAllBroadcastList()
        var recyclerView: RecyclerView? = null
        if(broadCasts.size > 0 || adapter == null) recyclerView = showBroadcastRecycler(view, broadCasts)

        if(broadCasts.size <= 0) view.findViewById<View>(R.id.no_broadcast).visibility = View.VISIBLE

        return recyclerView
    }

    private fun getAllBroadcastList() =  Broadcast.getBroadcast("all", context as Context)
    private fun showBroadcastRecycler(view: View, broadcasts: BroadcastListType): RecyclerView {
        val recyclerView: RecyclerView = view.findViewById(R.id.no_broadcast_recycler_view)


        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter<BroadcastType>(broadcasts, R.layout.broadcast_item_row) { item, view ->
            view.current_date.text = item.createdAt
            view.broadcast_message.text = item.message

            Log.i("RECYCLER BIND", "${view.toString()}, ${item.toString()}")

        }
        recyclerView.adapter = adapter
        return recyclerView
    }

    private fun addNewBroadcast(dialog: AlertDialog?) {
        val emergencyText = dialog?.findViewById<EditText>(messageViewId)
        val broadcast = Broadcast.newBroadcast(emergencyText?.text.toString(), context as Context)
        Log.i("Broadcast", broadcast.toString())

        SMS.send(broadcast.message)
        adapter?.updateAdapter(getAllBroadcastList())
    }

    private fun showAlert(view: View, location: String) {
        val builder = AlertDialog.Builder(context as Context)
        var dialog: AlertDialog? = null

        builder.setView(getEmergencyMessageView(location))

        builder.setTitle("Watcher Emergency Message")
        builder.setMessage("You can Edit this message. Click send when done")
        builder.setPositiveButton("Send") {_, _ -> addNewBroadcast(dialog)}
        builder.setNeutralButton("Cancel", null)
        dialog = builder.create()

        dialog.show()
    }

    private fun getEmergencyMessageView(extraText: String): View {
        val editText = EditText(context)
        editText.id = messageViewId
        editText.setText("${LocationService.getEmergencyMessage()}. Checkout Location $extraText")

        return editText
    }

    private fun getEmergencyMessage(): String {
        return "I need your assistance right now. Attach is my current location. Thanks"
    }

    private fun getLocationOnGoogleMap(lat: String, long: String): String = "https://www.google.com/maps/place/$lat, $long"

    private fun newBroadcast(view: View) {
        Log.i("Broadcast", "new Broadcast")
        LocationService(context as Context, activity as Activity).getCurrentLocation {lat, long ->
            Log.i("returned location", "$long, $lat")
            showAlert(view, LocationService.getLocationOnGoogleMap(lat, long))
        }
    }

    private fun addEventListeners(view: View) {
        val newBroadcastButton: FloatingActionButton = view.findViewById(R.id.new_broadcast)

        newBroadcastButton.setOnClickListener{
            newBroadcast(it)
        }

    }
}
