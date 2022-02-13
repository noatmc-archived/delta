package delta.util

class StringUtils {
    companion object {
        @JvmStatic
        fun toCamelCase(s: String): String {
            val parts = s.split("_").toTypedArray()
            var camelCaseString = ""
            for (part in parts) {
                camelCaseString += toProperCase(part)
            }
            return camelCaseString
        }
        @JvmStatic
        private fun toProperCase(s: String): String {
            return s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase()
        }
    }
}