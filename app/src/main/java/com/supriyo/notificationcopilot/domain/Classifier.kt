package com.supriyo.notificationcopilot.domain

enum class Category { MAIL, FINANCIAL, SOCIAL, ORDERS, PROMO, SYSTEM, OTHER }

enum class Subcategory { DEVELOPMENT, PROMOTIONAL, PERSONAL, DEBIT, CREDIT, NONE }

data class Classification(
    val category: Category,
    val subcategory: Subcategory,
    val important: Boolean
)

fun classify(packageName: String, title: String?, text: String?): Classification {
    val titleText = title.orEmpty()
    val bodyText = text.orEmpty()
    val allText = "$titleText $bodyText".lowercase()
    val packageLower = packageName.lowercase()
    val isSms = packageLower == MESSAGING_PACKAGE
    val isSmsSenderException = isSms && titleText.endsWith("-P", ignoreCase = true)

    if (packageLower in SYSTEM_PACKAGES || SYSTEM_PHRASES.any(allText::contains)) {
        return result(Category.SYSTEM, text = allText)
    }

    val amountInBody = AMOUNT_PATTERN.containsMatchIn(bodyText)
    if (amountInBody) {
        val financialTransaction = when {
            "paid you" in allText || "credited" in allText || "has credit" in allText ||
                "credit for" in allText ||
                ("received" in allText && STATUS_WORDS.any(allText::contains)) ||
                ("refund" in allText && STATUS_WORDS.any(allText::contains)) ->
                Subcategory.CREDIT
            "debited" in allText || "has debit" in allText || "debit for" in allText ||
                "spent" in allText || "withdrawn" in allText || "purchase of" in allText ||
                "paid to" in allText || ("payment" in allText && "successful" in allText) ->
                Subcategory.DEBIT
            else -> null
        }
        if (financialTransaction != null) {
            return result(
                Category.FINANCIAL,
                financialTransaction,
                text = allText,
                financialTransaction = true
            )
        }
    }

    if (
        listOf("add biller", "add billers", "add your biller", "settle your bills")
            .any(allText::contains)
    ) {
        return result(Category.PROMO, text = allText)
    }

    val financialNone = !isSmsSenderException && (
        "autopay" in allText ||
        BANK_MARKERS.any { marker ->
            Regex("""\b${Regex.escape(marker)}\b""").containsMatchIn(allText)
        } ||
            ("a/c" in allText && amountInBody)
        )
    if (financialNone) {
        return result(Category.FINANCIAL, text = allText, financialNone = true)
    }

    if (
        (isSms && SMS_PROMO_SENDER.matches(titleText)) ||
        (bodyText.trim().equals("Image", ignoreCase = true) && !financialNone) ||
        PROMO_PHRASES.any(allText::contains) ||
        PROMO_SENDER_MARKERS.any(allText::contains) ||
        packageLower in DEFAULT_PROMO_PACKAGES
    ) {
        return result(Category.PROMO, text = allText)
    }

    if (ORDER_PHRASES.any(allText::contains)) {
        return result(Category.ORDERS, text = allText)
    }

    if (packageLower == GMAIL_PACKAGE) {
        val subcategory = when {
            titleText.contains(Regex("""copilot|github|heroku|vercel""", RegexOption.IGNORE_CASE)) ||
                "pr #" in allText ||
                "pull request" in allText ||
                "build failed" in allText ||
                REPOSITORY_PATTERN.containsMatchIn(allText) -> Subcategory.DEVELOPMENT
            MAIL_PROMOTIONAL_MARKERS.any(allText::contains) -> Subcategory.PROMOTIONAL
            else -> Subcategory.PERSONAL
        }
        return result(Category.MAIL, subcategory, text = allText)
    }

    if (packageLower in SOCIAL_PACKAGES && !titleText.contains("barbeque nation", ignoreCase = true)) {
        return result(
            Category.SOCIAL,
            text = allText,
            socialPersonal = !titleText.contains(Regex("""\(\d+ messages\)"""))
        )
    }

    if (packageLower == "com.google.android.calendar") {
        return Classification(Category.OTHER, Subcategory.NONE, important = true)
    }

    return result(Category.OTHER, text = allText)
}

private fun result(
    category: Category,
    subcategory: Subcategory = Subcategory.NONE,
    text: String,
    financialTransaction: Boolean = false,
    financialNone: Boolean = false,
    socialPersonal: Boolean = false
): Classification {
    val important = when {
        category == Category.SYSTEM || category == Category.PROMO -> false
        category == Category.FINANCIAL && financialTransaction ->
            FINANCIAL_IMPORTANT_MARKERS.any(text::contains)
        category == Category.FINANCIAL && financialNone -> false
        category == Category.SOCIAL && socialPersonal ->
            SOCIAL_IMPORTANT_MARKERS.any(text::contains)
        category == Category.OTHER && "mdr charges" in text -> false
        category == Category.OTHER && text.isEmpty() -> false
        else -> IMPORTANT_PATTERN.containsMatchIn(text)
    }
    return Classification(category, subcategory, important)
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
private val STATUS_WORDS = listOf("initiated", "processed", "credited")
private val BANK_MARKERS = listOf("sbi", "icici", "hdfc", "axis", "kotak", "bank", "cbsalerts")
private val PROMO_PHRASES = listOf(
    "offer", "% off", "flat ", "cashback", "sale", "discount", "deal", "limited time",
    "shop now", "get up to", "starting @", "sitewide", "reward", "apply code", "use code",
    "code:", "book now", "refer", "earn up to", "save up to", "add biller",
    "settle your bills", "new openings"
)
private val PROMO_SENDER_MARKERS = listOf(
    "buyhatke", "jiopc", "jioreach", "jiomotive", "jiomart offers"
)
private val ORDER_PHRASES = listOf(
    "order", "shipped", "out for delivery", "delivered", "pick up", "pick-up", "pickup",
    "awb", "tracking", "on the way", "on the move"
)
private val MAIL_PROMOTIONAL_MARKERS = listOf(
    "unstop", "bigbasket", "sbi", "kaggle", "leetcode", "linkedin", "puter", "fly.io",
    "myntra updates", "team", "newsletter", "digest", "weekly", "new on", "competition",
    "hiring"
)
private val SOCIAL_PACKAGES = setOf(
    "com.whatsapp", "org.telegram.messenger", "instagram", "facebook", "twitter", "snapchat"
)
private val DEFAULT_PROMO_PACKAGES = setOf(
    "net.one97.paytm",
    "com.google.android.apps.nbu.paisa.user",
    "paytm",
    "paisa.user",
    "phonepe",
    "com.jamhub.barbeque",
    // The CSV has no package ID for this app; retain the supplied label until verified.
    "max app"
)
private val FINANCIAL_IMPORTANT_MARKERS =
    listOf("failed", "declined", "low balance", "overdue", "due")
private val SOCIAL_IMPORTANT_MARKERS =
    listOf("urgent", "deadline", "exam", "registration", "hw")

private const val MESSAGING_PACKAGE = "com.google.android.apps.messaging"
private const val GMAIL_PACKAGE = "com.google.android.gm"
private val SMS_PROMO_SENDER = Regex("""[A-Z]{2}-.+-P""")
private val AMOUNT_PATTERN = Regex("""(?:₹|rs\.?|inr)\s*[\d,]+(?:\.\d+)?""", RegexOption.IGNORE_CASE)
private val REPOSITORY_PATTERN = Regex("""\[[^]]+/[^]]+]""")
private val IMPORTANT_PATTERN = Regex(
    """\b\d{1,2}(?:st|nd|rd|th)?\s+(?:jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)\b""" +
        """|\b(?:jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)\s+\d{1,2}(?:st|nd|rd|th)?\b""" +
        """|\b\d{1,2}(?:st|nd|rd|th)\b""" +
        """|\b\d{1,2}[-/]\d{1,2}[-/]\d{2,4}\b""" +
        """|\b(?:tomorrow|today|tonight|within\s+\d+\s+minutes?|[0-9]{1,2}\s*(?:am|pm))\b""" +
        """|\b(?:deadline|due|last date|urgent|last day|pickup|pick up|pick-up|registration|exam|hw|out for delivery|action required|reminder:|security alert|sign-in|google account|mfa|verification|overdue|failed|declined|low balance)\b""",
    RegexOption.IGNORE_CASE
)
