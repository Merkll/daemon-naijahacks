package com.example.watcher.model

import java.util.Observable

class Auth: Observable() {
    /// The first name of the user
    var phoneNumber: String = ""
        set(value) {
            field = value
            setChangedAndNotify("phoneNumber")
        }

    /// The last name of the user
    var password: String = ""
        set(value) {
            field = value
            setChangedAndNotify("password")
        }

    private fun setChangedAndNotify(field: Any)
    {
        setChanged()
        notifyObservers(field)
    }
}