package com.monkeydp.tools.useful

import org.apache.commons.lang3.StringUtils
import java.math.BigInteger
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Compares dotted version strings of varying length. Makes a best effort with
 * other delimiters and non-numeric versions.
 *
 *
 * For dotted decimals, comparison is as you'd expect: 0.1 is before 0.2 is before
 * 1.0 is before 2.0. This works for any number of dots.
 *
 *
 * More complicated version numbers are compared by splitting the version strings
 * into components using the [.DELIMITER_PATTERN] and comparing each
 * component in order. The first difference found when comparing components
 * left-to-right is returned.
 *
 *
 * Two numeric components (containing only digits) are compared as integers. A
 * numeric component comes after any non-numeric one. Two non-numeric components
 * are ordered by [String.compareToIgnoreCase].
 */
internal class VersionStringComparator : Comparator<String> {
    /**
     * Compares two version strings using the algorithm described above.
     *
     * @return <tt>-1</tt> if version1 is before version2, <tt>1</tt> if version2 is before
     * version1, or <tt>0</tt> if the versions are equal.
     * @throws IllegalArgumentException if either argument does not match [.VALID_VERSION_PATTERN].
     * @see .isValidVersionString
     */
    override fun compare(version1: String,
                         version2: String): Int { // Get the version numbers, remove all whitespaces
        var thisVersion = "0"
        if (StringUtils.isNoneEmpty(version1)) {
            thisVersion = version1.replace(" ".toRegex(), "")
        }
        var compareVersion = "0"
        if (StringUtils.isNoneEmpty(version2)) {
            compareVersion = version2.replace(" ".toRegex(), "")
        }
        require(!(!thisVersion.matches(VALID_VERSION_PATTERN.toRegex()) || !compareVersion.matches(VALID_VERSION_PATTERN.toRegex()))) { "Version number '$thisVersion' cannot be compared to '$compareVersion'" }
        // Split the version numbers
        val v1: Array<String> =
                thisVersion.split(DELIMITER_PATTERN.toRegex())
                        .toTypedArray()
        val v2: Array<String> =
                compareVersion.split(DELIMITER_PATTERN.toRegex())
                        .toTypedArray()
        val componentComparator: Comparator<String> =
                VersionStringComponentComparator()
        // Compare each place, until we find a difference and then return. If empty, assume zero.
        for (i in 0 until if (v1.size > v2.size) v1.size else v2.size) {
            val component1 = if (i >= v1.size) "0" else v1[i]
            val component2 = if (i >= v2.size) "0" else v2[i]
            if (componentComparator.compare(component1, component2) != 0) {
                return componentComparator.compare(component1, component2)
            }
        }
        return 0
    }
    
    private class VersionStringComponentComparator : Comparator<String> {
        companion object Factory {
            private const val FIRST_GREATER = 1
            private const val SECOND_GREATER = -1
        }
        
        override fun compare(component1: String, component2: String): Int {
            if (component1.equals(component2, ignoreCase = true)) {
                return 0
            }
            if (isInteger(component1) && isInteger(component2)) {
                return BigInteger(component1).compareTo(BigInteger(component2))
            }
            // Handles the case where we are comparing 1.5 to 1.6a
            val comp1BigIntPart = getStartingInteger(component1)
            val comp2BigIntPart = getStartingInteger(component2)
            if (comp1BigIntPart != null && comp2BigIntPart != null) {
                if (comp1BigIntPart > comp2BigIntPart) {
                    return FIRST_GREATER
                }
                if (comp2BigIntPart > comp1BigIntPart) {
                    return SECOND_GREATER
                }
            }
            // 2.3-alpha < 2.3.0 and 2.3a < 2.3
// fixes PLUG-672. We are safe to do the integer check here since above we have
// already determined that one of the two components are not an integer and that one does not start with
// an int that may be larger than the other component
            if (isInteger(component1)) {
                return FIRST_GREATER
            }
            return if (isInteger(component2)) {
                SECOND_GREATER
            } else component1.compareTo(component2, ignoreCase = true)
            // 2.3a < 2.3b
        }
        
        private fun isInteger(string: String): Boolean {
            return string.matches("\\d+".toRegex())
        }
        
        private fun getStartingInteger(string: String): BigInteger? {
            val matcher: Matcher =
                    START_WITH_INT_PATTERN.matcher(string)
            return if (matcher.find()) { // If we found a starting digit group then lets return it
                BigInteger(matcher.group(1))
            } else null
        }
    }
    
    companion object {
        const val DELIMITER_PATTERN = "[.-]"
        private const val COMPONENT_PATTERN = "[\\d\\w]+"
        const val VALID_VERSION_PATTERN: String =
                COMPONENT_PATTERN + "(?:" + DELIMITER_PATTERN + COMPONENT_PATTERN + ")*"
        private val START_WITH_INT_PATTERN = Pattern.compile("(^\\d+)")
        private val SNAPSHOT_PATTERN = Pattern.compile(".*-SNAPSHOT$")
        fun isValidVersionString(version: String?): Boolean {
            return version != null && version.matches(VALID_VERSION_PATTERN.toRegex())
        }
        
        fun isSnapshotVersion(version: String?): Boolean {
            return version != null && SNAPSHOT_PATTERN.matcher(version).matches()
        }
    }
}