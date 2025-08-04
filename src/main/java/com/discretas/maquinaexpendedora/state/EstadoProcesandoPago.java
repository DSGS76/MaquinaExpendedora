package com.discretas.maquinaexpendedora.state;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Transaccion;

/**
 * Estado donde el pago es suficiente y se espera confirmación para proceder.
 *
 * @author Duvan Gil
 * @version 1.0
 */
public class EstadoProcesandoPago implements EstadoMaquina {

    @Override
    public String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto) {
        return "Ya hay una transacción en proceso. Complete o cancele la transacción actual.";
    }

    @Override
    public String insertarDinero(MaquinaExpendedora maquina, double monto) {
        if (monto <= 0) {
            return "Error: El monto debe ser mayor a cero.";
        }

        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion != null) {
            double nuevoMonto = transaccion.getMontoPagado() + monto;
            transaccion.setMontoPagado(nuevoMonto);
            return "Dinero adicional insertado: $" + (int) monto + ". Total: $" + (int) nuevoMonto + ". Presione confirmar para continuar.";
        }
        return "Error: No hay transacción activa.";
    }

    @Override
    public String confirmarPago(MaquinaExpendedora maquina) {
        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion == null) {
            return "Error: No hay transacción activa.";
        }

        if (!transaccion.pagoEsSuficiente()) {
            return "Error: El pago no es suficiente.";
        }

        // Calcular cambio
        double cambio = transaccion.getMontoPagado() - transaccion.getProducto().getPrecio();
        transaccion.setCambio(cambio);

        // Verificar si hay suficiente cambio disponible
        if (cambio > 0 && !maquina.haySuficienteCambio(cambio)) {
            maquina.cambiarEstado(new EstadoSinCambio());
            return "Error: No hay suficiente cambio disponible. Transacción cancelada.";
        }

        // Proceder a dispensar
        maquina.cambiarEstado(new EstadoDispensando());
        return "Pago confirmado. Dispensando producto...";
    }

    @Override
    public String dispensarProducto(MaquinaExpendedora maquina) {
        return "Error: Debe confirmar el pago primero.";
    }

    @Override
    public String devolverCambio(MaquinaExpendedora maquina) {
        return "Error: Debe confirmar el pago primero.";
    }

    @Override
    public String cancelarTransaccion(MaquinaExpendedora maquina) {
        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion != null) {
            double montoDevolver = transaccion.getMontoPagado();
            transaccion.setEstado(Transaccion.EstadoTransaccion.CANCELADA);
            maquina.finalizarTransaccion();
            maquina.cambiarEstado(new EstadoSeleccionando());
            return "Transacción cancelada. Dinero devuelto: $" + (int) montoDevolver;
        }

        maquina.cambiarEstado(new EstadoSeleccionando());
        return "Transacción cancelada.";
    }

    @Override
    public String getNombreEstado() {
        return "PROCESANDO_PAGO";
    }
}
