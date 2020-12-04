package ru.mikov.test.extensions.data

import ru.mikov.test.data.local.entities.*
import ru.mikov.test.data.remote.res.*

fun CardsRes.toCard(): Card = Card(
    cardId = number,
    barcode = barcode.toBarcode(),
    issuer = issuer.toIssuer(),
    kind = kind,
    loyaltyCard = loyaltyCard?.toLoyaltyCard(),
    texture = texture.toTexture(),
    certificate = certificate?.toCetrificate(),
    numberOfUses = 0
)

fun CertificateRes.toCetrificate(): Certificate = Certificate(
    value = value,
    expireDate = expireDate
)

private fun TextureRes.toTexture(): Texture = Texture(
    back = back,
    front = front
)


private fun LoyaltyCardRes.toLoyaltyCard(): LoyaltyCard =
    LoyaltyCard(
        balance = balance,
        grade = grade
    )

private fun IssuerRes.toIssuer(): Issuer =
    Issuer(
        categories = categories,
        name = name
    )


fun BarcodeRes.toBarcode(): Barcode =
    Barcode(
        kind = kind,
        number = number
    )