package com.discretas.maquinaexpendedora.presentation.controller;

import com.discretas.maquinaexpendedora.models.Producto;
import com.discretas.maquinaexpendedora.models.Transaccion;
import com.discretas.maquinaexpendedora.presentation.dto.ApiResponseDTO;
import com.discretas.maquinaexpendedora.services.MaquinaService;
import com.discretas.maquinaexpendedora.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones de la máquina expendedora.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@RestController
@RequestMapping(Constants.Global.API_BASE_PATH + Constants.Maquina.MAQUINA_SERVICE_PATH)
@RequiredArgsConstructor
@Slf4j
public class MaquinaController {

    private final MaquinaService maquinaService;

    /**
     * Obtiene el estado actual de la máquina
     */
    @GetMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_STATE)
    public ResponseEntity<?> obtenerEstado() {
        ApiResponseDTO<String> response = maquinaService.obtenerEstadoActual();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Obtiene todos los productos disponibles
     */
    @GetMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_PRODUCTS)
    public ResponseEntity<?> obtenerProductos() {
        ApiResponseDTO<Map<String, Producto>> response = maquinaService.obtenerProductosDisponibles();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Selecciona un producto
     */
    @PostMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_SELECT + "/{codigoProducto}")
    public ResponseEntity<?> seleccionarProducto(@PathVariable String codigoProducto) {
        log.info("Seleccionando producto: {}", codigoProducto);

        ApiResponseDTO<String> response = maquinaService.seleccionarProducto(codigoProducto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Inserta dinero en la máquina
     */
    @PostMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_INSERT_MONEY + "/{monto}")
    public ResponseEntity<?> insertarDinero(@PathVariable double monto) {
        log.info("Insertando dinero: ${}", monto);

        ApiResponseDTO<String> response = maquinaService.insertarDinero(monto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Confirma el pago
     */
    @PostMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_CONFIRM_PAYMENT)
    public ResponseEntity<?> confirmarPago() {
        ApiResponseDTO<String> response = maquinaService.confirmarPago();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Dispensa el producto
     */
    @PostMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_DISPENSE)
    public ResponseEntity<?> dispensarProducto() {
        ApiResponseDTO<String> response = maquinaService.dispensarProducto();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));

    }

    /**
     * Cancela la transacción actual
     */
    @PostMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_CANCEL)
    public ResponseEntity<?> cancelarTransaccion() {
        ApiResponseDTO<String> response = maquinaService.cancelarTransaccion();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));

    }

    /**
     * Obtiene la transacción actual
     */
    @GetMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_CURRENT_TRANSACTION)
    public ResponseEntity<?> obtenerTransaccionActual() {
        ApiResponseDTO<Transaccion> response = maquinaService.obtenerTransaccionActual();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));

    }

    /**
     * Obtiene el historial de transacciones
     */
    @GetMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_HISTORY)
    public ResponseEntity<?> obtenerHistorial() {
        ApiResponseDTO<List<Transaccion>> response = maquinaService.obtenerHistorialTransacciones();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));

    }

    /**
     * Reinicia la máquina
     */
    @PostMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_RESTART)
    public ResponseEntity<?> reiniciarMaquina() {
        log.info("Reiniciando máquina expendedora");

        ApiResponseDTO<String> response = maquinaService.reiniciarMaquina();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));

    }

    /**
     * Obtiene el dinero disponible en la máquina
     */
    @GetMapping(Constants.Maquina.MAQUINA_SERVICE_PATH_AVAILABLE_MONEY)
    public ResponseEntity<?> obtenerDineroDisponible() {
        ApiResponseDTO<Double> response = maquinaService.obtenerDineroDisponible();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
