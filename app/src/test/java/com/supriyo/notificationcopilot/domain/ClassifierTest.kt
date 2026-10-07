package com.supriyo.notificationcopilot.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassifierTest {
    @Test
    fun suppliedExamples_followClassificationRules() {
        assertClassification("com.whatsapp", "Checking for new messages", null, Category.SYSTEM, Subcategory.NONE, false)
        assertClassification("com.google.android.googlequicksearchbox", "Feels like 34° · Clear · See full forecast", null, Category.SYSTEM, Subcategory.NONE, false)
        assertClassification("com.google.android.deskclock", "Upcoming alarm Tue 5:40 am", null, Category.SYSTEM, Subcategory.NONE, false)
        assertClassification("paytm", "Get Up to ₹200 Cashback", null, Category.PROMO, Subcategory.NONE, false)
        assertClassification("com.whatsapp", "Flat 33% Off on Ariel, till 7th Oct", null, Category.PROMO, Subcategory.NONE, false)
        assertClassification("com.google.android.gm", "Hey Supriyo, happening now", "bigbasket", Category.MAIL, Subcategory.PROMOTIONAL, false)
        assertClassification("com.google.android.gm", "[Hiring Alert] Land a PPI", "Team Unstop", Category.MAIL, Subcategory.PROMOTIONAL, false)
        assertClassification("paisa.user", "EZAJ ASGAR paid you ₹20.00", null, Category.FINANCIAL, Subcategory.CREDIT, false)
        assertClassification("paisa.user", "Payment for Autopay of ₹89.00 to Google Play was successful", null, Category.FINANCIAL, Subcategory.DEBIT, false)
        assertClassification("messaging", "Your order SH5469707067 is out for delivery", null, Category.ORDERS, Subcategory.NONE, true)
        assertClassification("messaging", "We'll knock on your door by 12-10-2026 to pick up the product", null, Category.ORDERS, Subcategory.NONE, true)
        assertClassification("whatsapp", "Your order has been cancelled", null, Category.ORDERS, Subcategory.NONE, false)
        assertClassification("whatsapp", "your reservation on 12 Oct 2:30 PM is on hold, pay advance within 10 minutes", null, Category.OTHER, Subcategory.NONE, true)
        assertClassification("com.google.android.gm", "Re: [Supriyo-SP/CampusOps] Development (PR #5)", "Copilot", Category.MAIL, Subcategory.DEVELOPMENT, false)
        assertClassification("com.google.android.gm", "Remember to Register a Backup MFA Verification Method", "Heroku Notifications", Category.MAIL, Subcategory.DEVELOPMENT, false)
        assertClassification("com.google.android.gm", "segment - 7 done", "Supriyo Pal", Category.MAIL, Subcategory.PERSONAL, false)
        assertClassification("com.google.android.gm", "Registration Confirmed – Claude Workshop at 7 PM", "Zoom", Category.MAIL, Subcategory.PERSONAL, true)
        assertClassification("com.whatsapp", "Ok", "Mala Mess", Category.SOCIAL, Subcategory.NONE, false)
        assertClassification("com.whatsapp", "Tui jbi ?", null, Category.SOCIAL, Subcategory.NONE, false)
        assertClassification("com.whatsapp", "Date: 10th October 2026 Time: 7:30 Pm Online", null, Category.SOCIAL, Subcategory.NONE, true)
        assertClassification("com.whatsapp", "Only 2 videos will be ok for this week. HW", null, Category.SOCIAL, Subcategory.NONE, true)
    }

    private fun assertClassification(
        packageName: String,
        text: String,
        title: String?,
        category: Category,
        subcategory: Subcategory,
        important: Boolean
    ) {
        assertEquals(
            Classification(category, subcategory, important),
            classify(packageName, title, text)
        )
    }
}
