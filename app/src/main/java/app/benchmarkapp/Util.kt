package app.benchmarkapp

class Util {
    companion object {
        fun extractRegexFromString(regex: String, input: String): String?{
            val regexPattern = Regex(regex)
            return regexPattern.find(input)?.groupValues?.get(1)?.trim()
        }
    }
}