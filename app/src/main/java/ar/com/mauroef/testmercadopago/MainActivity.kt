package ar.com.mauroef.testmercadopago

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvPublicKey = findViewById<TextView>(R.id.tvPublicKey)
        val tvPreferenceId = findViewById<TextView>(R.id.tvPreferenceId)

        // Credenciales
        tvPublicKey.text = "YourMPKey"
        tvPreferenceId.text = "YourPreferenceID"

        btMercadoPagoCheckout.setOnClickListener {
            startMercadoPagoCheckout(
                tvPublicKey.text.toString(), tvPreferenceId.text.toString()
            )
        }
    }

    fun startMercadoPagoCheckout(publicKey: String, preferenceId: String) {
        MercadoPagoCheckout.Builder(publicKey, preferenceId).build()
            .startPayment(this, REQUEST_CODE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val message: String = when {
                resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE -> {
                    val payment =
                        data?.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT) as? Payment
                    "Resultado: ${payment?.paymentStatus} - $payment"
                }
                resultCode == Activity.RESULT_CANCELED && (data?.extras?.containsKey(
                    MercadoPagoCheckout.EXTRA_ERROR
                ) ?: false) -> {
                    val mercadoPagoError =
                        data?.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR) as? MercadoPagoError
                    "Error: ${mercadoPagoError?.message}"
                }
                else -> {
                    "Pago Cancelado"
                }
            }

            tvResult.text = message
        }
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}

