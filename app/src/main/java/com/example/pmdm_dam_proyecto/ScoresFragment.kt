package com.example.pmdm_dam_proyecto

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.color.utilities.Score

class ScoresFragment : Fragment() {
    private val FILAS_KEY = "punutacionMasAlta"
    private val filas = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedInstanceState?.let {
            filas.addAll(it.getStringArrayList(FILAS_KEY) ?: emptyList())
        }
        (requireActivity() as MainActivity).activarMenuLateral()
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(FILAS_KEY, filas as ArrayList<String>)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val puntuacionMasAlta = arguments?.getInt("puntuacionMasAlta", 0) ?: 0
        if (puntuacionMasAlta != 0) {
            añadirFila(puntuacionMasAlta)
        }
    }

    private fun añadirFila(puntuacion: Int) {
        val tabla = view?.findViewById<TableLayout>(R.id.tableLayout)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Nombre del ganador")
        val puntuacionTextView = TextView(requireContext())
        val nombre = EditText(requireContext())

        builder.setView(nombre)
        builder.setPositiveButton("OK") { _, _ ->
            puntuacionTextView.text = puntuacion.toString()
            val fila = TableRow(requireContext())
            val nuevoNombre = TextView(requireContext())
            nuevoNombre.text = nombre.text
            nuevoNombre.setTextColor(resources.getColor(R.color.white))
            puntuacionTextView.setTextColor(resources.getColor(R.color.white))
            fila.addView(nuevoNombre)
            fila.addView(puntuacionTextView)
            if (nombre.parent != null) {
                (nombre.parent as? ViewGroup)?.removeView(nombre)
            }
            tabla?.addView(fila)
        }
        builder.show()
        val sharedPreferences = requireContext().getSharedPreferences("puntuaciones", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("puntuacionMasAlta", puntuacion)
        editor.apply()
    }

}

