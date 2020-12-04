package ru.mikov.test.ui.dialogs

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.bottom_sheet.*
import ru.mikov.test.R
import ru.mikov.test.extensions.visible

class BarcodeDialog : BottomSheetDialogFragment() {

    private val args: BarcodeDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            tv_error.visible()
        }

    }
}