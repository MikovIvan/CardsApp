package ru.mikov.test.data.remote.res

import java.util.*

data class CardsRes(
    val barcode: BarcodeRes,
    val issuer: IssuerRes,
    val kind: String,
    val loyaltyCard: LoyaltyCardRes? = null,
    val number: String,
    val texture: TextureRes,
    val certificate: CertificateRes? = null
)

data class BarcodeRes(
    val kind: String,
    val number: String
)

data class IssuerRes(
    val categories: List<String> = listOf(),
    val name: String
)

data class LoyaltyCardRes(
    val balance: Int,
    val grade: String
)

data class TextureRes(
    val back: String,
    val front: String
)

data class CertificateRes(
    val value: Int,
    val expireDate: Date
)