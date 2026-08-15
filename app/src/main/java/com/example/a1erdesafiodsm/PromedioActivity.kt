package com.example.a1erdesafiodsm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

class PromedioActivity : AppCompatActivity() {

    private val idCanal = "canal_promedio"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)

        crearCanalNotificaciones()

        val txtNombre = findViewById<TextInputEditText>(R.id.etNombreEstudiante)
        val txtNota1 = findViewById<TextInputEditText>(R.id.etNota1)
        val txtNota2 = findViewById<TextInputEditText>(R.id.etNota2)
        val txtNota3 = findViewById<TextInputEditText>(R.id.etNota3)
        val txtNota4 = findViewById<TextInputEditText>(R.id.etNota4)
        val txtNota5 = findViewById<TextInputEditText>(R.id.etNota5)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularPromedio)
        val txtResultado = findViewById<TextView>(R.id.tvResultado)
        val btnRegresar = findViewById<Button>(R.id.btnRegresarMenu)

        btnRegresar.setOnClickListener { finish() }

        btnCalcular.setOnClickListener {
            val listaCampos = listOf(txtNota1, txtNota2, txtNota3, txtNota4, txtNota5)
            var hayError = false

            if (txtNombre.text.toString().trim().isEmpty()) {
                txtNombre.error = getString(R.string.error_campo_vacio)
                hayError = true
            }

            val notas = mutableListOf<Double>()
            for (campo in listaCampos) {
                val valorTexto = campo.text.toString().trim()
                if (valorTexto.isEmpty()) {
                    campo.error = getString(R.string.error_campo_vacio)
                    hayError = true
                } else {
                    val num = valorTexto.toDoubleOrNull()
                    if (num == null || num < 0 || num > 10) {
                        campo.error = getString(R.string.error_nota_invalida)
                        hayError = true
                    } else {
                        notas.add(num)
                    }
                }
            }

            if (!hayError && notas.size == 5) {
                val promedioFinal = calcularPromedio(notas[0], notas[1], notas[2], notas[3], notas[4])
                val df = DecimalFormat("#.##")
                val promFormateado = df.format(promedioFinal)
                val estado = if (promedioFinal >= 6.0) "Aprobado" else "Reprobado"

                txtResultado.text = "Estudiante: ${txtNombre.text}\nPromedio: $promFormateado\nEstado: $estado"
                mostrarNotificacion("Resultado del Promedio", "$estado con nota de $promFormateado")
            }
        }
    }

    private fun calcularPromedio(n1: Double, n2: Double, n3: Double, n4: Double, n5: Double): Double {
        return (n1 * 0.15) + (n2 * 0.15) + (n3 * 0.20) + (n4 * 0.25) + (n5 * 0.25)
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(idCanal, "Promedios", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(canal)
        }
    }

    private fun mostrarNotificacion(titulo: String, msj: String) {
        val builder = NotificationCompat.Builder(this, idCanal)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(msj)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }
}