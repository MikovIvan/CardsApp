package ru.mikov.test.ui.card

import android.graphics.Bitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_card.*
import ru.mikov.test.R
import ru.mikov.test.extensions.gone
import ru.mikov.test.extensions.invisible
import ru.mikov.test.extensions.visible
import ru.mikov.test.ui.base.BaseFragment
import ru.mikov.test.viewmodel.card.CardViewModel

class CardFragment : BaseFragment<CardViewModel>() {
    override val viewModel: CardViewModel by viewModels()
    override val layout: Int = R.layout.fragment_card
    private val args: CardFragmentArgs by navArgs()

    override fun setupViews() {
        Glide.with(this)
            .load(args.cardImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv_card)

        var bitmap: Bitmap? = null
        val barcodeEncoder = BarcodeEncoder()
        when (args.barcodeKind) {
            "EAN13" -> {
                if (args.barcodeNumber.length == 13) {
                    bitmap = barcodeEncoder.encodeBitmap(args.barcodeNumber, BarcodeFormat.EAN_13, 800, 200)
                }
            }
            "CODE128" -> {
                bitmap = barcodeEncoder.encodeBitmap(args.barcodeNumber, BarcodeFormat.CODE_128, 800, 200)
            }
            "QR" -> {
                bitmap = barcodeEncoder.encodeBitmap(args.barcodeNumber, BarcodeFormat.QR_CODE, 400, 400)
            }
        }
        if (bitmap != null) {
            Glide.with(requireContext())
                .load(bitmap)
                .into(iv_card_barcode)
        } else {
            iv_card_barcode.invisible()
            tv_error_card.visible()
        }

        tv_card_balance.text = args.cardBalance.toString()
    }


}