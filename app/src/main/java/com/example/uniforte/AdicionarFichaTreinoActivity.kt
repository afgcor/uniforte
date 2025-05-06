package com.example.uniforte

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup

class AdicionarFichaTreinoActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var etTituloFicha: EditText
    private lateinit var etDescricaoFicha: EditText
    private lateinit var spSelecioneExercicios: Spinner
    private lateinit var btnSalvarFicha: Button
    private lateinit var cgListaExercicios: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_ficha_treino)

        btnVoltar = findViewById(R.id.btnVoltar)
        etTituloFicha = findViewById(R.id.etTituloFicha)
        etDescricaoFicha = findViewById(R.id.etDescricaoFicha)
        spSelecioneExercicios = findViewById(R.id.spSelecioneExercicios)
        btnSalvarFicha = findViewById(R.id.btnSalvarFicha)
        cgListaExercicios = findViewById(R.id.cgListaExercicios)

        val adaptador = ArrayAdapter.createFromResource(this, R.array.lista_exercicios, android.R.layout.simple_spinner_item)

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSelecioneExercicios.adapter = adaptador

    }
}