package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load


class HomeAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_aluno)


        // Visualizar todos os agendamentos
        findViewById<TextView>(R.id.tvVerTudo).setOnClickListener {
            startActivity(Intent(this, AgendamentosActivity::class.java))
        }

         // Botão para agendar nova aula
         findViewById<Button>(R.id.btnAgendarNovaAula).setOnClickListener {
             startActivity(Intent(this, AulasDisponiveisActivity::class.java))
         }

        // Navegação inferior: Item "Ficha de Treino"
        findViewById<TextView>(R.id.navFicha).setOnClickListener {
            startActivity(Intent(this, FichaTreinoActivity::class.java))
        }

        // Navegação inferior: Item "Perfil"
        findViewById<TextView>(R.id.navPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }


        findViewById<TextView>(R.id.navHome).setOnClickListener {
        }

        //  Redirecionar para FeedbackActivity
        findViewById<ImageView>(R.id.ivFeedbackCalorico).setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
    }
}
