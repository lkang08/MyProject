package com.lk.myproject.room.encrypt

import android.text.TextUtils
import com.lk.myproject.ext.log
import java.util.concurrent.ConcurrentHashMap

class AccountEncrypt {

    val PASSWORD_PREFIX = "C5DF6AA##"
    val PASSWORD_SUFFIX = "#AA6FD5C"
    private var mEncryptedMap = ConcurrentHashMap<String, String>()
    private var mDecryptedMap = ConcurrentHashMap<String, String>()
    private var mEncryptKey: String? = null

    companion object {
        const val TAG = "AccountEncrypt"
    }

    fun getIdentifyEncryptContent(encryptContent: String): String {
        if (encryptContent.startsWith(PASSWORD_PREFIX)) {
            return encryptContent
        }
        return PASSWORD_PREFIX + encryptContent + PASSWORD_SUFFIX
    }

    fun getRealEncryptContent(encryptContent: String?): String {
        return encryptContent?.replaceFirst(PASSWORD_PREFIX.toRegex(), "")
            ?.replace(PASSWORD_SUFFIX, "") ?: ""
    }

    fun decodeContent(encryptedContent: String?): String {
        return if (TextUtils.isEmpty(encryptedContent)) {
            ""
        } else try {
            val cacheDecrypt = mDecryptedMap[encryptedContent]
            if (!TextUtils.isEmpty(cacheDecrypt)) {
                return cacheDecrypt!!
            }
            val raw = AesUtils.aesDecrypt(encryptedContent, getEncryptKey())
            mDecryptedMap[encryptedContent!!] = raw
            raw
        } catch (e: Throwable) {
            encryptedContent ?: ""
        }
    }

    fun getEncryptKey(): String {
        if (TextUtils.isEmpty(mEncryptKey)) {
            try {
                mEncryptKey = MisKey.getUpKeySting() + MisKey.getDownKeySting()
            } catch (throwable: Throwable) {
                "getEncryptKey err:".log()
            }
        }
        return mEncryptKey!!
    }

    fun encodeContent(raw: String?): String {
        return if (TextUtils.isEmpty(raw)) {
            ""
        } else try {
            val cacheEncrypt = mEncryptedMap[raw]
            if (!TextUtils.isEmpty(cacheEncrypt)) {
                return cacheEncrypt!!
            }
            var encoded = AesUtils.aesEncrypt(raw, getEncryptKey())
            if (encoded == null) {
                encoded = raw ?: ""
            } else {
                mEncryptedMap[raw!!] = encoded
            }
            encoded
        } catch (e: Throwable) {
            "encode err:".log()
            raw ?: ""
        }
    }

    fun decodeContentCompat(content: String?): String {
        if (TextUtils.isEmpty(content)) {
            return content!!
        }
        if (content?.startsWith(PASSWORD_PREFIX) == true && content.endsWith(PASSWORD_SUFFIX)) {
            val result = decodeContent(getRealEncryptContent(content))
            if (result.startsWith(PASSWORD_PREFIX)) {
                "double encrypt?$content $result".log()
                return decodeContent(getRealEncryptContent(content))
            }
            return result
        }
        return content ?: ""
    }

    fun encodeContent(plainText: String?, decorate: Boolean): String {
        return if (decorate) {
            val c = encodeContent(plainText)
            return if (TextUtils.isEmpty(c)) {
                ""
            } else {
                getIdentifyEncryptContent(c)
            }
        } else {
            encodeContent(plainText)
        }
    }

    fun safeEncodeContent(plainText: String?, decorate: Boolean): String {
        if (plainText?.startsWith(PASSWORD_PREFIX) == true && plainText.endsWith(PASSWORD_SUFFIX)) {
            return plainText
        }
        return encodeContent(plainText, decorate)
    }

    /**
     * 仅仅用于年龄 * 3 + 100000后再加高位7
     * 年龄加密
     */
    fun safeEncodeAge(value: Int): Int {
        val length = value.toString()
        if (length.length >= 6) return value
        val hexBefore = MathUtils.toHex(100000 + value * 3) ?: return value
        //如果高位是7直接返回
        if (hexBefore.startsWith("7")) return value
        //否则加入个高位7,
        return MathUtils.hexToInt("7$hexBefore")
    }

    /**
     * 年龄解密
     */
    fun safeDecodeAge(int: Int): Int {
        val length = int.toString()
        //小于7位非加密直接返回
        if (length.length < 7) return int
        val hexBefore = MathUtils.toHex(int) ?: return int
        //如果高位没有7直接返回
        if (!hexBefore.startsWith("7")) return int
        //否则移除高位7
        val reallyHex = hexBefore.replaceFirst("7", "")
        return MathUtils.hexToInt(reallyHex) % 100000 / 3
    }
}