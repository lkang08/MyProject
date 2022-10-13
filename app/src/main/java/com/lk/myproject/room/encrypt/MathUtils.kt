package com.lk.myproject.room.encrypt

/**
 * Time:2022/2/11 3:36 下午
 * Author:hx
 * Description:
 */
object MathUtils {
    /**
     * 10转16进制
     *
     * @param value
     * @return
     */
    fun toHex(value: Int): String? {
        return try {
            Integer.toHexString(value)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 16进制转10进制
     *
     * @param value
     * @return
     */
    fun hexToInt(value: String?): Int {
        return try {
            Integer.valueOf(value, 16)
        } catch (e: Exception) {
            0
        }
    }
}