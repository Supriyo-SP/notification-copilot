package com.supriyo.notificationcopilot.domain

enum class Category {
    MAIL,
    FINANCIAL,
    SOCIAL,
    ORDERS,
    PROMO,
    SYSTEM,
    OTHER
}

enum class Subcategory {
    DEVELOPMENT,
    PROMOTIONAL,
    PERSONAL,
    DEBIT,
    CREDIT,
    NONE
}

data class Classification(
    val category: Category,
    val subcategory: Subcategory,
    val important: Boolean
)

fun classify(packageName: String, title: String?, text: String?): Classification {
    val allText = listOfNotNull(title, text).joinToString(" ").lowercase()
    val packageNameLower = packageName.lowercase()

    if (
        packageNameLower in SYSTEM_PACKAGES ||
        SYSTEM_PHRASES.any(allText::contains)
    ) {
        return classification(Category.SYSTEM, text = allText)
    }

    if (PROMO_PHRASES.any(allText::contains)) {
        return classification(Category.PROMO, text = allText)
    }

    val financialSubcategory = when {
        "paid you" in allText || "credited" in allText -> Subcategory.CREDIT
        PAYMENT_TO_SUCCESSFUL.containsMatchIn(allText) ||
            "debited" in allText -> Subcategory.DEBIT
        MONEY_PHRASES.any(allText::contains) -> Subcategory.NONE
        else -> null
    }
    if (financialSubcategory != null) {
        return classification(Category.FINANCIAL, financialSubcategory, allText)
    }

    if (ORDER_PHRASES.any(allText::contains)) {
        return classification(Category.ORDERS, text = allText)
    }

    if (packageNameLower == GMAIL_PACKAGE) {
        val subcategory = when {
            DEVELOPMENT_PHRASES.any(allText::contains) ||
                REPOSITORY_PATTERN.containsMatchIn(allText) -> Subcategory.DEVELOPMENT
            MAIL_PROMOTIONAL_PHRASES.any(allText::contains) -> Subcategory.PROMOTIONAL
            else -> Subcategory.PERSONAL
        }
        return classification(Category.MAIL, subcategory, allText)
    }

    if (packageNameLower in SOCIAL_PACKAGES) {
        return classification(Category.SOCIAL, text = allText)
    }

    return classification(Category.OTHER, text = allText)
}

private fun classification(
    category: Category,
    subcategory: Subcategory = Subcategory.NONE,
    text: String
): Classification {
    return Classification(
        category = category,
        subcategory = subcategory,
        important = category != Category.SYSTEM &&
            category != Category.PROMO &&
            IMPORTANT_PATTERN.containsMatchIn(text)
    )
}

private val SYSTEM_PACKAGES = setOf(
    "com.google.android.deskclock",
    "com.google.android.googlequicksearchbox",
    "com.android.vending",
    "com.google.android.dialer"
)

private val SYSTEM_PHRASES = listOf(
    "checking for new messages",
    "syncing new emails",
    "sensitive notification content hidden"
)

private val PROMO_PHRASES = listOf(
    "offer",
    "% off",
    "cashback",
    "sale",
    "discount",
    "limited time",
    "shop now",
    "get up to",
    "rewards",
    "book now",
    "new openings"
)

private val MONEY_PHRASES = listOf("a/c", "upi", "receipt", "refund")

private val ORDER_PHRASES = listOf(
    "order",
    "shipped",
    "out for delivery",
    "delivered",
    "pick up",
    "tracking",
    "delivery otp"
)

private val DEVELOPMENT_PHRASES = listOf(
    "github",
    "pull request",
    "pr #",
    "heroku",
    "build failed",
    "developer"
)

private val MAIL_PROMOTIONAL_PHRASES = listOf(
    "unstop",
    "bigbasket",
    "sbi",
    "team",
    "newsletter"
)

private val SOCIAL_PACKAGES = setOf(
    "com.whatsapp",
    "org.telegram.messenger",
    "instagram",
    "facebook",
    "twitter",
    "snapchat"
)

private const val GMAIL_PACKAGE = "com.google.android.gm"
private val PAYMENT_TO_SUCCESSFUL = Regex("""payment\b.*\bto\b.*\bwas successful""")
private val REPOSITORY_PATTERN = Regex("""\[[^]]+/[^]]+]""")
private val IMPORTANT_PATTERN = Regex(
    """\b\d{1,2}(?:st|nd|rd|th)?\s+(?:jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)\b""" +
        """|\b\d{1,2}[-/]\d{1,2}[-/]\d{2,4}\b""" +
        """|\b\d{1,2}:\d{2}\s*(?:am|pm)\b""" +
        """|\b\d{1,2}\s*(?:am|pm)\b""" +
        """|\bwithin\s+\d+\s+minutes?\b""" +
        """|\b(?:tomorrow|tonight|deadline|due|last date|urgent|last day|pickup|registration|exam|hw|out for delivery|otp)\b"""
)
