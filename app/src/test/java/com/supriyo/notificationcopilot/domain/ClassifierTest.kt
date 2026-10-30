package com.supriyo.notificationcopilot.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassifierTest {
    @Test
    fun classifierV2Examples() {
        check("Your A/C XXXXX has credit for VREF of Rs 499.50 on 09/10/26. Avl Bal Rs 868.74.-SBI", "JD-CBSSBI-S", "com.google.android.apps.messaging", Category.FINANCIAL, Subcategory.CREDIT, false)
        check("Refund of Rs. 299.5 has been initiated & will reflect in 5-7 business days", "JD-SHEINI-S", "com.google.android.apps.messaging", Category.FINANCIAL, Subcategory.CREDIT, false)
        check("EZAJ paid you ₹20.00", null, "com.google.android.apps.nbu.paisa.user", Category.FINANCIAL, Subcategory.CREDIT, false)
        check("Payment for Autopay of ₹1.00 to JioStar was successful", "Payment to JioStar was successful", "com.google.android.apps.nbu.paisa.user", Category.FINANCIAL, Subcategory.DEBIT, false)
        check("Autopay created up to ₹149.00 to JioStar. Expires Oct 5, 2036", "Autopay created", "com.google.android.apps.nbu.paisa.user", Category.FINANCIAL, Subcategory.NONE, false)
        check("You cancelled your Autopay to JioStar", "Autopay cancelled", "com.google.android.apps.nbu.paisa.user", Category.FINANCIAL, Subcategory.NONE, false)
        check("CBSSBI ALERT", "cbsalerts.sbi", "com.google.android.gm", Category.FINANCIAL, Subcategory.NONE, false)
        check("Image", "ICICI Bank Savings Account", "com.google.android.apps.messaging", Category.FINANCIAL, Subcategory.NONE, false)
        check("Get a 3X refund if your train ticket is unconfirmed", "Worried you'll be waitlisted?", "phonepe", Category.PROMO, Subcategory.NONE, false)
        check("UPI is free for customers. Merchant UPI above Rs. 2000 will attract MDR charges from 15/10/26", "No UPI MDR Charges for customers", "lotusintouch", Category.OTHER, Subcategory.NONE, false)
        check("Pay ₹100 or more of your Credit Card Bill on Paytm. Apply code X & get a FREE workshop", "Your Bill Is Investing in You", "paytm", Category.PROMO, Subcategory.NONE, false)
        check("Add Your Biller on YONO SBI For Quick Payments", "SBI", "com.google.android.gm", Category.PROMO, Subcategory.NONE, false)
        check("Dear Supriya, Settle your bills in seconds on Yono! Add billers now", "JK-SBIBNK-P", "com.google.android.apps.messaging", Category.PROMO, Subcategory.NONE, false)
        check("Mega Home Fest is LIVE! Upto 50% OFF", "VM-HOMCEN-P", "com.google.android.apps.messaging", Category.PROMO, Subcategory.NONE, false)
        check("Image", "JioPC Offers", "com.google.android.apps.messaging", Category.PROMO, Subcategory.NONE, false)
        check("Image", "JioReach", "com.google.android.apps.messaging", Category.PROMO, Subcategory.NONE, false)
        check("Enjoy Flat 400 OFF", "Festive Kurta Sets", "max app", Category.PROMO, Subcategory.NONE, false)
        check("Starting @ ₹199", "Clogs, Flip Flops, Flats", "max app", Category.PROMO, Subcategory.NONE, false)
        check("Compare & Book from Uber, Ola, Rapido. Save up to 27%", "Buyhatke finds you the best ride price", "buyhatke", Category.PROMO, Subcategory.NONE, false)
        check("Get your TV match-ready!", "IND vs WI: Match Time!", "paytm", Category.PROMO, Subcategory.NONE, false)
        check("Shop at TecPods and redeem it now", "You have an expiring reward", "paisa.user", Category.PROMO, Subcategory.NONE, false)
        check("We'll knock on your door by 12-10-2026 to pick up the product... refund as soon as pickup is done", "JX-SHEINI-S", "com.google.android.apps.messaging", Category.ORDERS, Subcategory.NONE, true)
        check("PICK-UP DONE: AWB with Qty 1 from Shein B2C", "JD-XPBEES-S", "com.google.android.apps.messaging", Category.ORDERS, Subcategory.NONE, true)
        check("Your products are on the way", "Max Fashion", "com.google.android.gm", Category.ORDERS, Subcategory.NONE, false)
        check("Your Myntra order is out for delivery", "Myntra Updates", "com.google.android.gm", Category.ORDERS, Subcategory.NONE, true)
        check("React JS and Node JS Developer at Infosys", "LinkedIn", "com.google.android.gm", Category.MAIL, Subcategory.PROMOTIONAL, false)
        check("Competition Launch: NFL Big Data Bowl 2027", "Kaggle", "com.google.android.gm", Category.MAIL, Subcategory.PROMOTIONAL, false)
        check("LeetCode Weekly Digest", "LeetCode", "com.google.android.gm", Category.MAIL, Subcategory.PROMOTIONAL, false)
        check("New on Puter: Claude Code", "Puter", "com.google.android.gm", Category.MAIL, Subcategory.PROMOTIONAL, false)
        check("Re: [Supriyo-SP/CampusOps] Feat/pagination (PR #8)", "Copilot", "com.google.android.gm", Category.MAIL, Subcategory.DEVELOPMENT, false)
        check("Action required: Keep your older Flow creations", "Google Flow", "com.google.android.gm", Category.MAIL, Subcategory.PERSONAL, true)
        check("You shared some Google Account data with Claude", "Google", "com.google.android.gm", Category.MAIL, Subcategory.PERSONAL, true)
        check("Reminder: Update your Google User Research profile by October 12th", "Google User Experience R.", "com.google.android.gm", Category.MAIL, Subcategory.PERSONAL, true)
        check("", "Practice DSA", "com.google.android.calendar", Category.OTHER, Subcategory.NONE, true)
        check("Otp", "chat name", "com.whatsapp", Category.SOCIAL, Subcategory.NONE, false)
        check("Khete jete 3 te bajbe", "chat name", "com.whatsapp", Category.SOCIAL, Subcategory.NONE, false)
        check("Aj theke meal off thakbe", "Mess group (5 messages): Name", "com.whatsapp", Category.SOCIAL, Subcategory.NONE, false)
        check("Up to 40% off Smartphones", "chat name", "com.whatsapp", Category.PROMO, Subcategory.NONE, false)
        check("Delivery Update: Your Myntra order is out for delivery", "chat name", "com.whatsapp", Category.ORDERS, Subcategory.NONE, true)
        check("Dear Customer, We apologize for the delay in pickup for your Order", "chat name", "com.whatsapp", Category.ORDERS, Subcategory.NONE, true)
        check("Hi, your reservation on 12 Oct 2:30 PM is on hold, pay advance within 10 minutes", "Barbeque Nation", "com.whatsapp", Category.OTHER, Subcategory.NONE, true)
        check("DATA Usage Alert! You have used 50% of daily data quota", "JV-JioPay-S", "com.google.android.apps.messaging", Category.OTHER, Subcategory.NONE, false)
    }

    private fun check(
        text: String,
        title: String?,
        packageName: String,
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
