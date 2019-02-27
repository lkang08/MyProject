package com.lk.myproject.activity

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Data
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.lk.myproject.R
import com.lk.permissionutils.PermissionListener
import com.lk.permissionutils.PermissionsUtil
import kotlinx.android.synthetic.main.activity_main2.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class PermissionTestActivity : BaseActivity() {

    companion object {
        var time: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn_camera.setOnClickListener {
            requestCamera()
        }

        Thread.sleep(time)
        Log.d("lk###","time = $time")
        time += 100

        btn_read_contact.setOnClickListener {
            requestReadContact()
        }

        btn_sms.setOnClickListener { requestSms() }

        btn_write_contact.setOnClickListener { requestWriteContact() }
    }


    private fun requestCamera() {
        PermissionsUtil.requestPermission(application, object : PermissionListener {
            override fun permissionGranted(permissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "访问摄像头", Toast.LENGTH_LONG).show()
            }

            override fun permissionDenied(permissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "用户拒绝了访问摄像头", Toast.LENGTH_LONG).show()
            }
        }, Manifest.permission.CAMERA)
    }


    private fun requestReadContact() {
        val tip = PermissionsUtil.TipInfo("注意:", "我就是想看下你的通讯录", "不让看", "打开权限")
        PermissionsUtil.requestPermission(this, object : PermissionListener {
            override fun permissionGranted(permissions: Array<String>) {
                var arr: JSONArray? = null
                try {
                    arr = getContactInfo(this@PermissionTestActivity)
                    if (arr!!.length() == 0) {
                        Toast.makeText(this@PermissionTestActivity, "请确认通讯录不为空且有访问权限", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@PermissionTestActivity, arr.toString(), Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun permissionDenied(permissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "用户拒绝了读取通讯录权限", Toast.LENGTH_LONG).show()
            }
        }, arrayOf(Manifest.permission.READ_CONTACTS), true, tip)
    }

    private fun requestSms() {

        PermissionsUtil.requestPermission(this, object : PermissionListener {
            override fun permissionGranted(permissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "访问消息", Toast.LENGTH_LONG).show()
            }

            override fun permissionDenied(permissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "用户拒绝了读取消息权限", Toast.LENGTH_LONG).show()
            }
        }, arrayOf(Manifest.permission.READ_SMS), false, null)
    }


    private fun requestWriteContact() {
        PermissionsUtil.requestPermission(this, object : PermissionListener {
            override fun permissionGranted(permissions: Array<String>) {
                if (addContact("test", "12345678909")) {
                    Toast.makeText(this@PermissionTestActivity, "成功添加联系人", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@PermissionTestActivity, "添加联系人失败", Toast.LENGTH_LONG).show()
                }
            }

            override fun permissionDenied(permissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "用户拒绝了写通讯录", Toast.LENGTH_LONG).show()
            }
        }, Manifest.permission.WRITE_CONTACTS)
    }


    // 一个添加联系人信息的例子
    private fun addContact(name: String, phoneNumber: String): Boolean {
        // 创建一个空的ContentValues
        val values = ContentValues()

        val rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
                ?: return false

        val rawContactId = ContentUris.parseId(rawContactUri)
        values.clear()

        values.put(Data.RAW_CONTACT_ID, rawContactId)
        // 内容类型
        values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name)
        // 向联系人URI添加联系人名字
        contentResolver.insert(Data.CONTENT_URI, values)
        values.clear()

        values.put(Data.RAW_CONTACT_ID, rawContactId)
        values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        // 向联系人电话号码URI添加电话号码
        contentResolver.insert(Data.CONTENT_URI, values)
        values.clear()

        values.put(Data.RAW_CONTACT_ID, rawContactId)
        values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        // 联系人的Email地址
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "test@163.com")
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
        // 向联系人Email URI添加Email数据
        contentResolver.insert(Data.CONTENT_URI, values)

        return true
    }

    @Throws(JSONException::class)
    private fun getContactInfo(activity: Activity): JSONArray? {
        // 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
        val contacts = mutableListOf<ContactInfo>()
        var mimetype = ""
        var oldrid = -1
        var contactId = -1
        val cursor = activity.contentResolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.Data.RAW_CONTACT_ID)
        var numm = 0
        var contact: ContactInfo? = null

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)
                if (index < 0) {
                    cursor.close()
                    return null
                }
                contactId = cursor.getInt(index)
                if (oldrid != contactId) {
                    contact = ContactInfo()
                    contacts.add(contact)
                    numm++
                    oldrid = contactId
                }
                // 取得mimetype类型
                mimetype = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE))

                /************* 获取通讯录中名字  */
                if (ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE == mimetype) {
                    val name = StringBuilder()
                    val firstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
                    if (!TextUtils.isEmpty(firstName)) {
                        name.append(firstName)
                    }

                    val middleName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME))
                    if (!TextUtils.isEmpty(middleName)) {
                        name.append(middleName)
                    }

                    val lastname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                    if (!TextUtils.isEmpty(lastname)) {
                        name.append(lastname)
                    }

                    contact!!.n = name.toString()
                }

                /***************** 获取电话  */
                if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE == mimetype) {
                    if (contact!!.p == null) {
                        contact!!.p = ArrayList()
                    }
                    val mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    if (!TextUtils.isEmpty(mobile)) {
                        contact!!.p.add(mobile)
                    }
                }

                /******************** 获取第一个邮件  */
                if (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE == mimetype) {
                    if (!TextUtils.isEmpty(contact!!.m)) {
                        continue
                    }
                    val email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    contact!!.m = email ?: ""
                }

                /******************** 获取公司信息  */
                if (ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE == mimetype) {
                    // 取出组织类型
                    val orgType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE))
                    // 单位
                    if (orgType == ContactsContract.CommonDataKinds.Organization.TYPE_CUSTOM) {
                        val company = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY))
                        contact!!.o = company ?: ""
                    }
                }

                /******************** 获取地址  */
                if (ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE == mimetype) {
                    if (!TextUtils.isEmpty(contact!!.a)) {
                        continue
                    }
                    // 取出类型
                    val postalType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE))
                    // 住宅通讯地址
                    if (postalType == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME) {
                        val homeStreet = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET))
                        contact!!.a = homeStreet ?: ""
                    }
                    // 单位通讯地址
                    if (postalType == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK) {
                        val street = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET))
                        contact!!.a = street ?: ""
                    }
                    // 其他通讯地址
                    if (postalType == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER) {
                        val otherStreet = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET))
                        contact!!.a = otherStreet ?: ""
                    }
                }
            }
        }
        cursor!!.close()
        val arr = JSONArray()
        for (con in contacts) {

            val sb = StringBuilder()
            sb.append(con.n)
            if (con.p != null && con.p.size > 0) {
                sb.append(con.p.get(0))
            }
            arr.put(sb.toString())
        }
        return arr
    }
}
