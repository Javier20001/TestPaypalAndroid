package com.test.paypalsegundointento

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import com.test.paypalsegundointento.databinding.ActivityMainBinding
import com.test.paypalsegundointento.ui.theme.PayPalSegundoIntentoTheme

class MainActivity : ComponentActivity() {
    lateinit var binding : ActivityMainBinding
    val TAG = "myTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        setContentView(R.layout.activity_main)

        var paymentButtonContainer: PaymentButtonContainer = findViewById(R.id.payment_button_container)

        paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "1.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i(TAG, "CaptureOrderResult: $captureOrderResult")
                    Toast.makeText(this,"payment approved", Toast.LENGTH_SHORT).show()
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "Buyer canceled the PayPal experience.")
                Toast.makeText(this,"payment cancelled", Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Log.d(TAG, "Error: $errorInfo")
                Toast.makeText(this,"payment ERROR", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
