package com.discretas.maquinaexpendedora.services;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Producto;
import com.discretas.maquinaexpendedora.models.Transaccion;
import com.discretas.maquinaexpendedora.presentation.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio que maneja la lógica de negocio de la máquina expendedora.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MaquinaService {

    private final MaquinaExpendedora maquina;

    /**
     * Obtiene el estado actual de la máquina
     * @return ApiResponseDTO con el estado actual de la máquina
     */
    public ApiResponseDTO<String> obtenerEstadoActual() {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            String estadoActual = maquina.getEstadoActualNombre();
            response.SuccessOperation(estadoActual);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Obtiene todos los productos disponibles
     * @return ApiResponseDTO con mapa de productos disponibles
     */
    public ApiResponseDTO<Map<String, Producto>> obtenerProductosDisponibles() {
        ApiResponseDTO<Map<String, Producto>> response = new ApiResponseDTO<>();
        try {
            Map<String, Producto> productos = maquina.getInventario();
            response.SuccessOperation(productos);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Selecciona un producto
     * @param codigoProducto Código del producto a seleccionar
     * @return ApiResponseDTO con mensaje del resultado
     */
    public ApiResponseDTO<String> seleccionarProducto(String codigoProducto) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            String resultado = maquina.seleccionarProducto(codigoProducto);
            response.SuccessOperation(resultado);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Inserta dinero en la máquina
     * @param monto Cantidad de dinero a insertar
     * @return ApiResponseDTO con mensaje del resultado
     */
    public ApiResponseDTO<String> insertarDinero(double monto) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            String resultado = maquina.insertarDinero(monto);
            response.SuccessOperation(resultado);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Confirma el pago de la transacción actual
     * @return ApiResponseDTO con mensaje del resultado
     */
    public ApiResponseDTO<String> confirmarPago() {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            String resultado = maquina.confirmarPago();
            response.SuccessOperation(resultado);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Dispensa el producto seleccionado
     * @return ApiResponseDTO con mensaje del resultado
     */
    public ApiResponseDTO<String> dispensarProducto() {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            String resultado = maquina.dispensarProducto();
            response.SuccessOperation(resultado);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Cancela la transacción actual
     * @return ApiResponseDTO con mensaje del resultado
     */
    public ApiResponseDTO<String> cancelarTransaccion() {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            String resultado = maquina.cancelarTransaccion();
            response.SuccessOperation(resultado);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Obtiene la transacción actual
     * @return ApiResponseDTO con la transacción actual
     */
    public ApiResponseDTO<Transaccion> obtenerTransaccionActual() {
        ApiResponseDTO<Transaccion> response = new ApiResponseDTO<>();
        try {
            Transaccion transaccion = maquina.getTransaccionActual();
            response.SuccessOperation(transaccion);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Obtiene el historial de transacciones
     * @return ApiResponseDTO con lista de transacciones realizadas
     */
    public ApiResponseDTO<List<Transaccion>> obtenerHistorialTransacciones() {
        ApiResponseDTO<List<Transaccion>> response = new ApiResponseDTO<>();
        try {
            List<Transaccion> historial = maquina.getHistorialTransacciones();
            response.SuccessOperation(historial);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Obtiene el dinero disponible en la máquina
     * @return Cantidad de dinero disponible para cambio
     */
    public ApiResponseDTO<Double> obtenerDineroDisponible() {
        ApiResponseDTO<Double> response = new ApiResponseDTO<>();
        try {
            double dineroDisponible = maquina.getDineroDisponible();
            response.SuccessOperation(dineroDisponible);
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }

    /**
     * Reinicia la máquina al estado inicial
     * @return ApiResponseDTO con mensaje de confirmación
     */
    public ApiResponseDTO<String> reiniciarMaquina() {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        try {
            if (maquina.getTransaccionActual() != null) {
                maquina.cancelarTransaccion();
            }
            response.SuccessOperation("Máquina reiniciada correctamente.");
        } catch (Exception e) {
            response.FailedOperation();
        }
        return response;
    }
}
