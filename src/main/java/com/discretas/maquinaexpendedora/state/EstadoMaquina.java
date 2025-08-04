package com.discretas.maquinaexpendedora.state;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;

/**
 * Interfaz que define el comportamiento de los estados de la máquina expendedora.
 * Implementa el patrón State para manejar los diferentes estados de la máquina.
 *
 * @author Duvan Gil
 * @version 1.0
 */
public interface EstadoMaquina {

    /**
     * Maneja la selección de un producto
     * @param maquina Referencia a la máquina expendedora
     * @param codigoProducto Código del producto seleccionado
     * @return Mensaje del resultado de la operación
     */
    String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto);

    /**
     * Maneja la inserción de dinero
     * @param maquina Referencia a la máquina expendedora
     * @param monto Cantidad de dinero insertada
     * @return Mensaje del resultado de la operación
     */
    String insertarDinero(MaquinaExpendedora maquina, double monto);

    /**
     * Maneja la confirmación del pago
     * @param maquina Referencia a la máquina expendedora
     * @return Mensaje del resultado de la operación
     */
    String confirmarPago(MaquinaExpendedora maquina);

    /**
     * Maneja la dispensación del producto
     * @param maquina Referencia a la máquina expendedora
     * @return Mensaje del resultado de la operación
     */
    String dispensarProducto(MaquinaExpendedora maquina);

    /**
     * Maneja la devolución del cambio
     * @param maquina Referencia a la máquina expendedora
     * @return Mensaje del resultado de la operación
     */
    String devolverCambio(MaquinaExpendedora maquina);

    /**
     * Maneja la cancelación de la transacción
     * @param maquina Referencia a la máquina expendedora
     * @return Mensaje del resultado de la operación
     */
    String cancelarTransaccion(MaquinaExpendedora maquina);

    /**
     * Obtiene el nombre del estado actual
     * @return Nombre del estado
     */
    String getNombreEstado();
}
