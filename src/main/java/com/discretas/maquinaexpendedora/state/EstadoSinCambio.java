package com.discretas.maquinaexpendedora.state;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Transaccion;

/**
 * Estado de error cuando no hay suficiente cambio disponible en la máquina.
 *
 * @author Duvan Gil
 * @version 1.0
 */
public class EstadoSinCambio implements EstadoMaquina {

    @Override
    public String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto) {
        return "La máquina tiene problemas con el cambio. Complete o cancele la transacción actual.";
    }

    @Override
    public String insertarDinero(MaquinaExpendedora maquina, double monto) {
        return "No se puede insertar más dinero. La máquina no tiene cambio suficiente.";
    }

    @Override
    public String confirmarPago(MaquinaExpendedora maquina) {
        return "No se puede confirmar el pago. La máquina no tiene cambio suficiente.";
    }

    @Override
    public String dispensarProducto(MaquinaExpendedora maquina) {
        return "No se puede dispensar el producto sin cambio suficiente.";
    }

    @Override
    public String devolverCambio(MaquinaExpendedora maquina) {
        return "No hay cambio suficiente disponible en la máquina.";
    }

    @Override
    public String cancelarTransaccion(MaquinaExpendedora maquina) {
        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion != null) {
            double montoDevolver = transaccion.getMontoPagado();
            transaccion.setEstado(Transaccion.EstadoTransaccion.CANCELADA);
            maquina.finalizarTransaccion();
            maquina.cambiarEstado(new EstadoSeleccionando());
            return "Transacción cancelada por falta de cambio. Dinero devuelto: $" + (int) montoDevolver;
        }

        maquina.cambiarEstado(new EstadoSeleccionando());
        return "Transacción cancelada.";
    }

    @Override
    public String getNombreEstado() {
        return "SIN_CAMBIO";
    }
}
