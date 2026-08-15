package com.example.a1erdesafiodsm

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.pow
import kotlin.math.sqrt

class CalculadoraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)

        val txtN1 = findViewById<TextInputEditText>(R.id.txtNum1)
        val txtN2 = findViewById<TextInputEditText>(R.id.txtNum2)
        val txtRes = findViewById<TextView>(R.id.txtResultadoCalc)

        val btnSuma = findViewById<Button>(R.id.btnSuma)
        val btnResta = findViewById<Button>(R.id.btnResta)
        val btnMulti = findViewById<Button>(R.id.btnMulti)
        val btnDiv = findViewById<Button>(R.id.btnDiv)
        val btnExpo = findViewById<Button>(R.id.btnExpo)
        val btnRaiz = findViewById<Button>(R.id.btnRaiz)
        val btnRegresar = findViewById<Button>(R.id.btnRegresarMenu)

        // Acción para volver al menú principal
        btnRegresar.setOnClickListener {
            finish()
        }

        btnSuma.setOnClickListener {
            val n1 = txtN1.text.toString().toDoubleOrNull()
            val n2 = txtN2.text.toString().toDoubleOrNull()
            if (n1 != null && n2 != null) {
                val res = n1 + n2
                txtRes.text = "Resultado: $res"
                guardarHistorial("$n1 + $n2 = $res")
            } else {
                txtN1.error = getString(R.string.error_campo_vacio)
            }
        }

        btnResta.setOnClickListener {
            val n1 = txtN1.text.toString().toDoubleOrNull()
            val n2 = txtN2.text.toString().toDoubleOrNull()
            if (n1 != null && n2 != null) {
                val res = n1 - n2
                txtRes.text = "Resultado: $res"
                guardarHistorial("$n1 - $n2 = $res")
            } else {
                txtN1.error = getString(R.string.error_campo_vacio)
            }
        }

        btnMulti.setOnClickListener {
            val n1 = txtN1.text.toString().toDoubleOrNull()
            val n2 = txtN2.text.toString().toDoubleOrNull()
            if (n1 != null && n2 != null) {
                val res = n1 * n2
                txtRes.text = "Resultado: $res"
                guardarHistorial("$n1 * $n2 = $res")
            } else {
                txtN1.error = getString(R.string.error_campo_vacio)
            }
        }

        btnDiv.setOnClickListener {
            val n1 = txtN1.text.toString().toDoubleOrNull()
            val n2 = txtN2.text.toString().toDoubleOrNull()
            if (n1 != null && n2 != null) {
                if (n2 == 0.0) {
                    txtN2.error = getString(R.string.error_div_cero)
                } else {
                    val res = n1 / n2
                    txtRes.text = "Resultado: $res"
                    guardarHistorial("$n1 / $n2 = $res")
                }
            } else {
                txtN1.error = getString(R.string.error_campo_vacio)
            }
        }

        btnExpo.setOnClickListener {
            val n1 = txtN1.text.toString().toDoubleOrNull()
            val n2 = txtN2.text.toString().toDoubleOrNull()
            if (n1 != null && n2 != null) {
                val res = n1.pow(n2)
                txtRes.text = "Resultado: $res"
                guardarHistorial("$n1 ^ $n2 = $res")
            } else {
                txtN1.error = getString(R.string.error_campo_vacio)
            }
        }

        btnRaiz.setOnClickListener {
            val n1 = txtN1.text.toString().toDoubleOrNull()
            if (n1 != null) {
                val res = sqrt(n1)
                txtRes.text = "Resultado: $res"
                guardarHistorial("√$n1 = $res")
            } else {
                txtN1.error = getString(R.string.error_campo_vacio)
            }
        }
    }

    private fun guardarHistorial(operacion: String) {
        val archivo = "historial_calculadora.txt"
        val linea = "$operacion\n"
        try {
            openFileOutput(archivo, Context.MODE_APPEND).use { output ->
                output.write(linea.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}