package com.discretas.maquinaexpendedora.state;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Producto;

/**
 * Estado inicial de la máquina expendedora.
 * La máquina está esperando que el usuario seleccione un producto.
 *
 * @author Duvan Gil
 * @version 1.0
 */
public class EstadoSeleccionando implements EstadoMaquina {

    @Override
    public String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto) {
        Producto producto = maquina.getInventario().get(codigoProducto);

        if (producto == null) {
            return "Error: Producto no encontrado. Código: " + codigoProducto;
        }

        if (!producto.estaDisponible()) {
            return "Error: Producto agotado. Seleccione otro producto.";
        }

        // Cambiar al estado de esperando pago
        maquina.cambiarEstado(new EstadoEsperandoPago());
        maquina.crearTransaccion(producto, 0.0);

        return "Producto seleccionado: " + producto.getNombre() + " - Precio: $" + (int) producto.getPrecio() + ". Inserte el dinero.";
    }

    @Override
    public String insertarDinero(MaquinaExpendedora maquina, double monto) {
        return "Error: Primero debe seleccionar un producto.";
    }

    @Override
    public String confirmarPago(MaquinaExpendedora maquina) {
        return "Error: Primero debe seleccionar un producto.";
    }

    @Override
    public String dispensarProducto(MaquinaExpendedora maquina) {
        return "Error: No hay producto seleccionado para dispensar.";
    }

    @Override
    public String devolverCambio(MaquinaExpendedora maquina) {
        return "Error: No hay transacción en proceso.";
    }

    @Override
    public String cancelarTransaccion(MaquinaExpendedora maquina) {
        return "No hay transacción para cancelar.";
    }

    @Override
    public String getNombreEstado() {
        return "SELECCIONANDO";
    }
}
