package delta.util

class TimeUtils {
    companion object {
        @JvmStatic
        fun toMs(nanoSecond: Long): Long {
            return nanoSecond / 1000000
        }
    }
}