package com.example.xmlprojectUsingDi.utils

/**
 * Extension function to fix malformed URLs where the base URL is duplicated
 * Example: https://s3.../dev-cmt/https://s3.../dev-cmt/4/profile/image.jpg
 * Returns: https://s3.../dev-cmt/4/profile/image.jpg
 */
fun String?.fixDuplicateUrl(): String? {
    if (this == null) return null

    // Find the second occurrence of "https://" (indicating duplication)
    val firstHttpsIndex = indexOf("https://")
    if (firstHttpsIndex == -1) return this

    val secondHttpsIndex = indexOf("https://", startIndex = firstHttpsIndex + 8)
    if (secondHttpsIndex == -1) return this

    // Extract the base URL (everything before the second "https://")
    val baseUrl = substring(0, secondHttpsIndex)

    // Extract the duplicated part (everything from second "https://" onwards)
    val duplicatedPart = substring(secondHttpsIndex)

    // Find where the bucket path ends in the duplicated part
    // Pattern: https://domain.com/bucket/path
    // We need to find the path after the bucket
    val domainEnd = duplicatedPart.indexOf("/", duplicatedPart.indexOf("://") + 3)
    if (domainEnd == -1) return this

    // Find the bucket end (second "/" after domain)
    val bucketEnd = duplicatedPart.indexOf("/", domainEnd + 1)
    if (bucketEnd == -1) return this

    // Extract the actual path after the bucket (e.g., "4/profile/image.jpg")
    val path = duplicatedPart.substring(bucketEnd + 1)

    // Remove trailing slash from baseUrl if present, then combine with path
    val cleanBaseUrl = baseUrl.trimEnd('/')
    return "$cleanBaseUrl/$path"
}
