package com.example.watcher.fragments

import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watcher.R
import com.example.watcher.adapters.RecyclerAdapter
import com.example.watcher.model.Contacts
import com.example.watcher.types.ContactListType
import com.example.watcher.types.ContactType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.contact_item_row.view.*


class ContactsFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: RecyclerAdapter<ContactType>? = null
    private val contactPickRequestCode = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_contacts, container, false)
        showContactsList(view)
        addEventListeners(view)

        return view
    }

    private fun addNewContact(view: View) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, contactPickRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when(requestCode) {
            contactPickRequestCode -> {
                val contactUri: Uri = data?.data as Uri;
                val contactDetails = getContactDetails(contactUri)[0]
                addNewContact(contactDetails?.get("phoneNumber"), contactDetails?.get("displayName"))
                // TODO: Add details to local db and  make post request
            }
        }
    }
    private fun  showContactsList(view: View): RecyclerView? {
        val contacts = getAllContactsList()
        var recyclerView: RecyclerView? = null
        if(contacts.size > 0 || adapter == null) recyclerView = showContactsRecycler(view, contacts)

        if(contacts.size <= 0) view.findViewById<View>(R.id.no_contacts).visibility = View.VISIBLE

        return recyclerView
    }

    private fun showContactsRecycler(view: View, contacts: ContactListType): RecyclerView {
        val recyclerView: RecyclerView = view.findViewById(R.id.contacts_recycler_view)


        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter<ContactType>(contacts, R.layout.contact_item_row) { item, view ->
            view.user_phone_number.text = item.phoneNumber
            view.display_name.text = item.displayName

        }

        recyclerView.adapter = adapter
        return recyclerView
    }


    private fun getAllContactsList() =  Contacts.getContacts("all", context as Context)


    private fun addNewContact(phoneNumber: String?, displayName: String?) {
        Contacts.newContact(phoneNumber as String, displayName as String, context as Context)
        adapter?.updateAdapter(getAllContactsList())
    }

    // TODO get list of emergency phone number and attach the name and picture
    private fun getContactList() {
        Log.i("ne contact", "..adding new")
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, contactPickRequestCode)
    }

    private fun addEventListeners(view: View) {
        val addContactButton: FloatingActionButton = view.findViewById(R.id.add_contact)

        addContactButton.setOnClickListener{
            addNewContact(it)
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun contentResolver(uri: Uri, projection: Array<String>?): Cursor? {
        return activity?.contentResolver?.query(uri, projection, null, null, null)
    }

    private fun contentResolver(uri: Uri, projection: Array<String?>, selection :String, selectionArgs: Array<String>, sortOrder: String?): Cursor? {
        return  activity?.contentResolver?.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    private fun getContactId(contactUri: Uri): String? {
        val cursor: Cursor? = contentResolver(contactUri, arrayOf(ContactsContract.Contacts._ID))
        cursor?.moveToFirst()
        val contactId: String? = cursor?.getString(cursor?.getColumnIndex(ContactsContract.Contacts._ID))

        cursor?.close()

        return contactId
    }

    private fun getContactDetails(contactUri: Uri): Array<Map<String, String>?> {
        val cursor: Cursor = contentResolver(contactUri, null) as Cursor
        var contactDetails = arrayOfNulls<Map<String, String>>(0)
        while (cursor.moveToNext()) {
            val displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val phoneNumber = getPhoneNumber(contactId) as String
            val details = mapOf("displayName" to displayName, "contactId" to contactId, "phoneNumber" to phoneNumber)
            contactDetails = contactDetails.plus(details)
        }
        return contactDetails
    }
    private fun getPhoneNumber(contactId: String?): String? {
        return when(contactId) {
            null -> null
            else -> {
                val cursor: Cursor? = contentResolver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    arrayOf<String>(contactId),
                    null
                )
                cursor?.moveToFirst()
                val phoneNumber: String? = cursor?.getString(cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                cursor?.close()

                return phoneNumber
            }
        }

    }
}
