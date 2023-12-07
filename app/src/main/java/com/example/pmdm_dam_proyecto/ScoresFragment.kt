package com.example.pmdm_dam_proyecto

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScoresFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scores, container, false)
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
        val nombre = TextView(requireContext())

        builder.setView(nombre)
        builder.setPositiveButton("OK") { _, _ ->
            puntuacionTextView.text = puntuacion.toString()

            val fila = TableRow(requireContext())
            val nuevoNombre = TextView(requireContext())
            nuevoNombre.text = nombre.text
            nuevoNombre.setTextColor(resources.getColor(R.color.white))
            fila.addView(nuevoNombre)
            fila.addView(puntuacionTextView)
            if (nombre.parent != null) {
                (nombre.parent as? ViewGroup)?.removeView(nombre)
            }
            tabla?.addView(fila)
        }
        builder.show()
    }

}

