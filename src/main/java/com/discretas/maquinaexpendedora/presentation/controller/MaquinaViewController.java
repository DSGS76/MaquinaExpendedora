package com.discretas.maquinaexpendedora.presentation.controller;

import com.discretas.maquinaexpendedora.services.MaquinaService;
import com.discretas.maquinaexpendedora.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web para servir las vistas estáticas de la máquina expendedora.
 * Toda la lógica de interacción se maneja via JavaScript y API REST.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@Controller
@RequiredArgsConstructor
public class MaquinaViewController {

    private final MaquinaService maquinaService;

    /**
     * Página principal de la máquina expendedora
     * Solo carga la vista inicial, el JS se encarga del resto
     */
    @GetMapping("/")
    public String index() {
        // No necesitamos pasar datos iniciales, todo se carga dinámicamente
        return "index";
    }

    /**
     * Página de historial
     * Carga inicial, luego el JS maneja la actualización dinámica
     */
    @GetMapping(Constants.Maquina.MAQUINA_SERVICE_PATH + Constants.Maquina.MAQUINA_SERVICE_PATH_HISTORY)
    public String historial(Model model) {
        // Mantenemos el historial para carga inicial
        model.addAttribute("historial", maquinaService.obtenerHistorialTransacciones().getData());
        return "maquina/historial";
    }
}
