package com.example.a1erdesafiodsm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.text.DecimalFormat

class PromedioActivity : AppCompatActivity() {

    private val CHANNEL_ID = "canal_promedio"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)

        crearCanalNotificaciones()

        val etNombre = findViewById<EditText>(R.id.etNombreEstudiante)
        val etNota1 = findViewById<EditText>(R.id.etNota1)
        val etNota2 = findViewById<EditText>(R.id.etNota2)
        val etNota3 = findViewById<EditText>(R.id.etNota3)
        val etNota4 = findViewById<EditText>(R.id.etNota4)
        val etNota5 = findViewById<EditText>(R.id.etNota5)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularPromedio)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnCalcular.setOnClickListener {
            val listaCampos = listOf(etNota1, etNota2, etNota3, etNota4, etNota5)
            var hayError = false

            if (etNombre.text.isEmpty()) {
                etNombre.error = getString(R.string.error_campo_vacio)
                hayError = true
            }

            val notas = mutableListOf<Double>()
            for (campo in listaCampos) {
                val texto = campo.text.toString()
                if (texto.isEmpty()) {
                    campo.error = getString(R.string.error_campo_vacio)
                    hayError = true
                } else {
                    val valor = texto.toDoubleOrNull()
                    if (valor == null || valor < 0 || valor > 10) {
                        campo.error = getString(R.string.error_nota_invalida)
                        hayError = true
                    } else {
                        notas.add(valor)
                    }
                }
            }

            if (!hayError && notas.size == 5) {
                val promedioFinal = calcularPromedio(notas[0], notas[1], notas[2], notas[3], notas[4])
                val df = DecimalFormat("#.##")
                val promedioFormateado = df.format(promedioFinal)
                val aprobado = promedioFinal >= 6.0
                val estado = if (aprobado) "Aprobado" else "Reprobado"

                val mensaje = "Estudiante: ${etNombre.text}\nPromedio: $promedioFormateado\nEstado: $estado"
                tvResultado.text = mensaje

                enviarNotificacion("Resultado del Promedio", "$estado con promedio de $promedioFormateado")
            }
        }
    }

    private fun calcularPromedio(n1: Double, n2: Double, n3: Double, n4: Double, n5: Double): Double {
        // Ponderaciones: 15%, 15%, 20%, 25%, 25%
        return (n1 * 0.15) + (n2 * 0.15) + (n3 * 0.20) + (n4 * 0.25) + (n5 * 0.25)
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones de Promedio",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(canal)
        }
    }

    private fun enviarNotificacion(titulo: String, mensaje: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }
}