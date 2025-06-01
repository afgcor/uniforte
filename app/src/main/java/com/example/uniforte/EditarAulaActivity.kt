package com.example.uniforte

import android.content.Intent
import java.util.Locale
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.util.Calendar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class EditarAulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_aula)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment   ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorProfessorFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeProfessorActivity::class.java))
                }
                R.id.navMeusAlunos -> {
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val inputDataAula = findViewById<TextInputEditText>(R.id.inputDataAula)
        inputDataAula.setOnClickListener {
            val c = Calendar.getInstance()
            val ano = c.get(Calendar.YEAR)
            val mes = c.get(Calendar.MONTH)
            val dia = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                val dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y)
                inputDataAula.setText(dataFormatada)
            }, ano, mes, dia)

            datePickerDialog.show()
        }

        val inputHorarioAula = findViewById<TextInputEditText>(R.id.inputHorarioAula)
        inputHorarioAula.setOnClickListener {
            val c = Calendar.getInstance()
            val hora = c.get(Calendar.HOUR_OF_DAY)
            val minuto = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, h, m ->
                val horarioFormatado = String.format(Locale.getDefault(), "%02d:%02d", h, m)
                inputHorarioAula.setText(horarioFormatado)
            }, hora, minuto, true)

            timePickerDialog.show()
        }

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnSalvar = findViewById<Button>(R.id.buttonSalvar)
        btnSalvar.setOnClickListener{
            finish()
            Toast.makeText(this, "Aula editada com sucesso!", Toast.LENGTH_SHORT).show()
        }

        val btnCancelar = findViewById<Button>(R.id.buttonCancelar)
        btnCancelar.setOnClickListener{
            finish()
        }
    }
}