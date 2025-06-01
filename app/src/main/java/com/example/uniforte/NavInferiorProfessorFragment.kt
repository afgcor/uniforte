package com.example.uniforte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class NavInferiorProfessorFragment : Fragment() {
    var onNavItemSelected: ((itemId: Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_inferior_professor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHome = view.findViewById<TextView>(R.id.navHome)
        val navMeusAlunos = view.findViewById<TextView>(R.id.navMeusAlunos)
        val navPerfilAdmin = view.findViewById<TextView>(R.id.navPerfilAdmin)

        navHome.setOnClickListener {
            onNavItemSelected?.invoke(R.id.navHome)
        }

        navMeusAlunos.setOnClickListener {
            onNavItemSelected?.invoke(R.id.navMeusAlunos)
        }

        navPerfilAdmin.setOnClickListener {
            onNavItemSelected?.invoke(R.id.navPerfilAdmin)
        }
    }
}