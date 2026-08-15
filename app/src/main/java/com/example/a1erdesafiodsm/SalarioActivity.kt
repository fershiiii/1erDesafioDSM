package com.example.a1erdesafiodsm

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

class SalarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario)

        val txtNombre = findViewById<TextInputEditText>(R.id.txtNombreEmp)
        val txtSalario = findViewById<TextInputEditText>(R.id.txtSalarioBase)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularSalario)
        val txtBruto = findViewById<TextView>(R.id.txtSalarioBrutoResultado)
        val txtDescuentos = findViewById<TextView>(R.id.txtDescuentosResultado)
        val txtNeto = findViewById<TextView>(R.id.txtSalarioNetoResultado)
        val btnRegresar = findViewById<Button>(R.id.btnRegresarMenu)

        btnRegresar.setOnClickListener { finish() }

        btnCalcular.setOnClickListener {
            val nom = txtNombre.text.toString().trim()
            val salTexto = txtSalario.text.toString().trim()

            if (nom.isEmpty()) {
                txtNombre.error = getString(R.string.error_campo_vacio)
                activarVibracion()
                return@setOnClickListener
            }

            if (salTexto.isEmpty()) {
                txtSalario.error = getString(R.string.error_campo_vacio)
                activarVibracion()
                return@setOnClickListener
            }

            val sueldoBase = salTexto.toDoubleOrNull()

            if (sueldoBase == null || sueldoBase <= 0) {
                txtSalario.error = getString(R.string.error_salario_invalida)
                activarVibracion()
            } else {
                val afp = sueldoBase * 0.0725
                val isss = if (sueldoBase > 1000) 30.0 else sueldoBase * 0.03
                val gravable = sueldoBase - afp - isss
                val renta = calcularRenta(gravable)

                val totalDescuento = afp + isss + renta
                val sueldoNeto = sueldoBase - totalDescuento

                val df = DecimalFormat("$#,##0.00")

                txtBruto.text = "Empleado: $nom\nSalario Bruto: ${df.format(sueldoBase)}"
                txtDescuentos.text = "Descuentos:\n • ISSS: ${df.format(isss)}\n • AFP: ${df.format(afp)}\n • Renta: ${df.format(renta)}\nTotal Descuentos: ${df.format(totalDescuento)}"
                txtNeto.text = "Salario Neto A Pagar: ${df.format(sueldoNeto)}"
            }
        }
    }

    private fun calcularRenta(monto: Double): Double {
        return when {
            monto <= 472.00 -> 0.0
            monto <= 895.24 -> ((monto - 472.00) * 0.10) + 17.67
            monto <= 2038.10 -> ((monto - 895.24) * 0.20) + 60.00
            else -> ((monto - 2038.10) * 0.30) + 288.57
        }
    }

    private fun activarVibracion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator.vibrate(android.os.VibrationEffect.createOneShot(400, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(400)
        }
    }
}