package com.discretas.maquinaexpendedora.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Modelo que representa un producto disponible en la máquina expendedora.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    /**
     * Identificador único del producto
     */
    private String codigo;

    /**
     * Nombre del producto
     */
    private String nombre;

    /**
     * Precio del producto en pesos
     */
    private double precio;

    /**
     * Cantidad disponible en stock
     */
    private int stock;

    /**
     * Descripción del producto
     */
    private String descripcion;

    /**
     * Verifica si el producto está disponible
     * @return true si hay stock disponible
     */
    public boolean estaDisponible() {
        return stock > 0;
    }

    /**
     * Reduce el stock del producto en 1 unidad
     */
    public void reducirStock() {
        if (stock > 0) {
            stock--;
        }
    }
}
