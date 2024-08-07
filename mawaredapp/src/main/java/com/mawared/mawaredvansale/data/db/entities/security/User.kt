package com.mawared.mawaredvansale.data.db.entities.security

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "User") // using this if table name different form entity name if same using below
@Entity // without parameter that's mean the table name is the same name for the entity name : User
data class User (
    var uid: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var user_name: String? = null,
    var password: String? = null,
    var email_verified_at: String? = null,
    var cl_Id: Int?,
    var org_Id: Int?,
    var cr_Id: Int?,
    var cr_code: String?,
    var sl_cr_Id: Int?,
    var sl_cr_code: String?,
    var ss_cr_Id: Int?,
    var ss_cr_code: String?,
    var sf_cr_Id: Int?,
    var sf_cr_code: String?,
    var isDirectRate: String?,
    var dropAmnt: Double?,
    var token: String?,
    var empId: Int?,
    var created_at: String? = null,
    var updated_at: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var client_name: String? = null
    var org_name: String? = null
    var iDiscPrcnt: Double = 0.0
    var dDiscPrcnt: Double = 0.0
    var hasGift: String? = null
    var print_msg: String? = null
    var qryGroup1: String? = null
    var qryGroup2: String? = null
    var qryGroup3: String? = null
}