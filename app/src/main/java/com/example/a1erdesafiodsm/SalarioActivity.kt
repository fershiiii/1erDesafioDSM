package com.example.a1erdesafiodsm

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class SalarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario)

        val txtNombre = findViewById<EditText>(R.id.txtNombreEmp)
        val txtSalario = findViewById<EditText>(R.id.txtSalarioBase)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularSalario)
        val txtBruto = findViewById<TextView>(R.id.txtSalarioBrutoResultado)
        val txtDescuentos = findViewById<TextView>(R.id.txtDescuentosResultado)
        val txtNeto = findViewById<TextView>(R.id.txtSalarioNetoResultado)

        btnCalcular.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val salarioTexto = txtSalario.text.toString()

            if (nombre.isEmpty()) {
                txtNombre.error = getString(R.string.error_campo_vacio)
                vibrarTelefono()
                return@setOnClickListener
            }

            if (salarioTexto.isEmpty()) {
                txtSalario.error = getString(R.string.error_campo_vacio)
                vibrarTelefono()
                return@setOnClickListener
            }

            val salarioBase = salarioTexto.toDoubleOrNull()

            if (salarioBase == null || salarioBase <= 0) {
                txtSalario.error = getString(R.string.error_salario_invalida)
                vibrarTelefono()
            } else {
                // Descuentos Ley El Salvador
                val afp = salarioBase * 0.0725
                val isss = if (salarioBase > 1000) 30.0 else salarioBase * 0.03

                // Base gravable para calcular Renta
                val montoGravable = salarioBase - afp - isss
                val renta = calcularRenta(montoGravable)

                val totalDescuentos = afp + isss + renta
                val salarioNeto = salarioBase - totalDescuentos

                val df = DecimalFormat("$#,##0.00")

                txtBruto.text = "Empleado: $nombre\nSalario Bruto: ${df.format(salarioBase)}"
                txtDescuentos.text = "Descuentos:\n - ISSS: ${df.format(isss)}\n - AFP: ${df.format(afp)}\n - Renta: ${df.format(renta)}\nTotal Descuentos: ${df.format(totalDescuentos)}"
                txtNeto.text = "Salario Neto Pagar: ${df.format(salarioNeto)}"
            }
        }
    }

    // Tabla de tramos de Renta oficial
    private fun calcularRenta(monto: Double): Double {
        return when {
            monto <= 472.00 -> 0.0
            monto <= 895.24 -> ((monto - 472.00) * 0.10) + 17.67
            monto <= 2038.10 -> ((monto - 895.24) * 0.20) + 60.00
            else -> ((monto - 2038.10) * 0.30) + 288.57
        }
    }

    private fun vibrarTelefono() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(android.os.VibrationEffect.createOneShot(500, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(500)
        }
    }
}