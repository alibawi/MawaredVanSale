package com.mawared.mawaredvansale.utilities

import android.content.Context
import android.content.SharedPreferences
import com.mawared.mawaredvansale.data.db.entities.security.User
import com.google.gson.Gson
import com.mawared.mawaredvansale.data.db.entities.md.Salesman
import com.mawared.mawaredvansale.data.db.entities.md.Warehouse
import com.mawared.mawaredvansale.data.db.entities.security.Menu


/*
 * Created by alibawi 2019-07-18
 */
class SharedPrefs(context: Context) {
    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, 0) // 0 means private

    val IS_LOGGED_IN = "isLoggedIn"
    val AUTH_TOKEN = "authToken"
    val USER_EMAIL = "userEmail"
    val KEY_SAVED_AT = "key_saved_at"
    val KEY_SAVED_USER = "key_saved_user"
    val KEY_SAVED_VAN_CODE = "key_saved_van_code"
    val KEY_SAVED_SALESMAN = "key_saved_salesman"
    val KEY_SAVED_WAREHOUSE = "key_saved_warehouse"
    val KEY_SYSTEM_LANG = "system_mawared_language"
    val LOGO_NAME = "client_logo_name"
    val KEY_BLUETOOTH_PRINTER_PORT = "bluetooth_printer_port"
    val KEY_BLUETOOTH_PRINTER_ADDR = "bluetooth_printer_addr"
    val KEY_PRINETER_NAME = "print_name"
    val KEY_LANG_ENCODE_LATIN = "lang_encode_latin"
    val KEY_LANG_CODEPAGE_LATIN = "lang_codepage_latin"
    val KEY_LANG_ENCODE_AR = "lang_encode_ar"
    val KEY_LANG_CODEPAGE_AR = "lang_codepage_ar"
    val KEY_BASE_SERVER_URL = ""
    val gson = Gson()
    val KEY_PRINTING_TYPE = "Key_priting_type"
    val KEY_PRINTER = "key_printer_type"
    val KEY_PRINTING_SO_MODE = "key_printing_so_mode"
    val KEY_PRINTING_INV_MODE = "key_printing_inv_mode"
    val KEY_APP_VERSION = "key_app_verion"
    val KEY_APP_HAS_PLAN = "key_app_has_plan"
    var KEY_SYS_MENU = "Key_sys_menu"
    var KEY_INVOICE_DISC = "Key_invoice_disc"
    var KEY_ITEM_DISC = "Key_item_disc"
    var KEY_API_IP = "Key_Api_ip"

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN, "")
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String?
        get() = prefs.getString(USER_EMAIL, "")
        set(value) = prefs.edit().putString(USER_EMAIL, value).apply()

    var savedAt: String?
        get() = prefs.getString(KEY_SAVED_AT, null)
        set(value) = prefs.edit().putString(KEY_SAVED_AT, value).apply()

    var saveUser: User?
        get() = gson.fromJson(prefs.getString(KEY_SAVED_USER, null), User::class.java)
        set(value) = prefs.edit().putString(KEY_SAVED_USER, gson.toJson(value)).apply()

    var savedVanCode: String?
        get() = prefs.getString(KEY_SAVED_VAN_CODE, null)
        set(value) = prefs.edit().putString(KEY_SAVED_VAN_CODE, value).apply()

    var savedSalesman: Salesman?
        get() = gson.fromJson(prefs.getString(KEY_SAVED_SALESMAN, null), Salesman::class.java)
        set(value) = prefs.edit().putString(KEY_SAVED_SALESMAN, gson.toJson(value)).apply()

    var savedWarehouse: Warehouse?
        get() = gson.fromJson(prefs.getString(KEY_SAVED_WAREHOUSE, null), Warehouse::class.java)
        set(value) = prefs.edit().putString(KEY_SAVED_WAREHOUSE, gson.toJson(value)).apply()

    var systemLanguage: String?
        get() = prefs.getString(KEY_SYSTEM_LANG, "")
        set(value) = prefs.edit().putString(KEY_SYSTEM_LANG, value).apply()
    var logo_image_name: String?
        get() = prefs.getString(LOGO_NAME, null)
        set(value) = prefs.edit().putString(LOGO_NAME, value).apply()

    var bluetooth_port: String?
        get() = prefs.getString(KEY_BLUETOOTH_PRINTER_PORT, null)
        set(value) = prefs.edit().putString(KEY_BLUETOOTH_PRINTER_PORT, value).apply()

    var bluetooth_address: String?
        get() = prefs.getString(KEY_BLUETOOTH_PRINTER_ADDR, null)
        set(value) = prefs.edit().putString(KEY_BLUETOOTH_PRINTER_ADDR, value).apply()

    var printer_name: String?
        get() = prefs.getString(KEY_PRINETER_NAME, null)
        set(value) = prefs.edit().putString(KEY_PRINETER_NAME, value).apply()

    var printing_type: String?
        get() = prefs.getString(KEY_PRINTING_TYPE, "R") // I - Invoice OR R-Receipt
        set(value) = prefs.edit().putString(KEY_PRINTING_TYPE, value).apply()
    var printer: String?
        get()= prefs.getString(KEY_PRINTER, "")
        set(value) = prefs.edit().putString(KEY_PRINTER, value).apply()
    var printing_so_mode: String?
        get() = prefs.getString(KEY_PRINTING_SO_MODE, "F") // F - Full OR H- Only Header
        set(value) = prefs.edit().putString(KEY_PRINTING_SO_MODE, value).apply()
    var printing_inv_mode: String?
        get() = prefs.getString(KEY_PRINTING_SO_MODE, "F") // F - Full OR H- Only Header
        set(value) = prefs.edit().putString(KEY_PRINTING_SO_MODE, value).apply()
    var lang_Encode_latin:String?
        get() = prefs.getString(KEY_LANG_ENCODE_LATIN, null)
        set(value) = prefs.edit().putString(KEY_LANG_ENCODE_LATIN, value).apply()

    var lang_CodePage_latin:String?
        get() = prefs.getString(KEY_LANG_CODEPAGE_LATIN, null)
        set(value) = prefs.edit().putString(KEY_LANG_CODEPAGE_LATIN, value).apply()

    var lang_Encode_ar:String?
        get() = prefs.getString(KEY_LANG_ENCODE_AR, null)
        set(value) = prefs.edit().putString(KEY_LANG_ENCODE_AR, value).apply()

    var lang_CodePage_ar:String?
        get() = prefs.getString(KEY_LANG_CODEPAGE_AR, null)
        set(value) = prefs.edit().putString(KEY_LANG_CODEPAGE_AR, value).apply()

    var server_url: String?
        get() = prefs.getString(KEY_BASE_SERVER_URL, null)
        set(value) = prefs.edit().putString(KEY_BASE_SERVER_URL, value).apply()

    var app_version: String?
        get() = prefs.getString(KEY_APP_VERSION, null)
        set(value) = prefs.edit().putString(KEY_APP_VERSION, value).apply()

    //hasSalesPlan
    var hasSalesPlan: String?
        get() = prefs.getString(KEY_APP_HAS_PLAN, null)
        set(value) = prefs.edit().putString(KEY_APP_HAS_PLAN, value).apply()

    var menuPref: String?
        get() = prefs.getString(KEY_SYS_MENU, null)
        set(value) = prefs.edit().putString(KEY_SYS_MENU, value).apply()

    var user_invoice_disc : String?
        get() = prefs.getString(KEY_INVOICE_DISC, "0.0")
         set(value) = prefs.edit().putString(KEY_INVOICE_DISC, value).apply()

    var user_item_disc : String?
        get() = prefs.getString(KEY_ITEM_DISC, "0.0")
        set(value) = prefs.edit().putString(KEY_ITEM_DISC, value).apply()

    var ApiIP : String?
        get() = prefs.getString(KEY_API_IP, null)
        set(value) = prefs.edit().putString(KEY_API_IP, value).apply()
}