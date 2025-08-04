package com.discretas.maquinaexpendedora.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Modelo que representa una transacción realizada en la máquina expendedora.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {

    /**
     * Identificador único de la transacción
     */
    private String id;

    /**
     * Producto seleccionado en la transacción
     */
    private Producto producto;

    /**
     * Monto total pagado por el cliente
     */
    private double montoPagado;

    /**
     * Cambio devuelto al cliente
     */
    private double cambio;

    /**
     * Fecha y hora de la transacción
     */
    private LocalDateTime fechaTransaccion;

    /**
     * Estado de la transacción (COMPLETADA, CANCELADA, EN_PROCESO)
     */
    private EstadoTransaccion estado;

    /**
     * Enumeración para los estados de la transacción
     */
    public enum EstadoTransaccion {
        EN_PROCESO,
        COMPLETADA,
        CANCELADA,
        ERROR
    }

    /**
     * Constructor para crear una nueva transacción
     */
    public Transaccion(String id, Producto producto, double montoPagado) {
        this.id = id;
        this.producto = producto;
        this.montoPagado = montoPagado;
        this.fechaTransaccion = LocalDateTime.now();
        this.estado = EstadoTransaccion.EN_PROCESO;
        this.cambio = calcularCambio();
    }

    /**
     * Calcula el cambio a devolver
     */
    private double calcularCambio() {
        if (producto != null) {
            return montoPagado - producto.getPrecio();
        } else {
            return montoPagado;
        }
    }

    /**
     * Verifica si el pago es suficiente
     */
    public boolean pagoEsSuficiente() {
        return producto != null && montoPagado >= producto.getPrecio();
    }
}
