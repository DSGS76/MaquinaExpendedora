package com.discretas.maquinaexpendedora.state;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Transaccion;

/**
 * Estado donde el usuario ha seleccionado un producto y puede insertar dinero.
 *
 * @author Duvan Gil
 * @version 1.0
 */
public class EstadoEsperandoPago implements EstadoMaquina {

    @Override
    public String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto) {
        return "Ya hay un producto seleccionado. Complete la transacción o cancélela.";
    }

    @Override
    public String insertarDinero(MaquinaExpendedora maquina, double monto) {
        if (monto <= 0) {
            return "Error: El monto debe ser mayor a cero.";
        }

        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion == null) {
            return "Error: No hay transacción activa.";
        }

        // Actualizar el monto pagado
        double nuevoMonto = transaccion.getMontoPagado() + monto;
        transaccion.setMontoPagado(nuevoMonto);

        // Verificar si es suficiente para pagar
        if (nuevoMonto >= transaccion.getProducto().getPrecio()) {
            maquina.cambiarEstado(new EstadoProcesandoPago());
            return "Dinero insertado: $" + (int) monto + ". Total: $" + (int) nuevoMonto + ". Presione confirmar para continuar.";
        } else {
            double faltante = transaccion.getProducto().getPrecio() - nuevoMonto;
            return "Dinero insertado: $" + (int) monto + ". Total: $" + (int) nuevoMonto + ". Falta: $" + (int) faltante;
        }
    }

    @Override
    public String confirmarPago(MaquinaExpendedora maquina) {
        return "Error: Inserte el dinero suficiente antes de confirmar.";
    }

    @Override
    public String dispensarProducto(MaquinaExpendedora maquina) {
        return "Error: Debe completar el pago primero.";
    }

    @Override
    public String devolverCambio(MaquinaExpendedora maquina) {
        return "Error: Debe completar el pago primero.";
    }

    @Override
    public String cancelarTransaccion(MaquinaExpendedora maquina) {
        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion != null) {
            double montoDevolver = transaccion.getMontoPagado();
            transaccion.setEstado(Transaccion.EstadoTransaccion.CANCELADA);
            maquina.finalizarTransaccion();
            maquina.cambiarEstado(new EstadoSeleccionando());

            if (montoDevolver > 0) {
                return "Transacción cancelada. Dinero devuelto: $" + (int) montoDevolver;
            } else {
                return "Transacción cancelada.";
            }
        }

        maquina.cambiarEstado(new EstadoSeleccionando());
        return "Transacción cancelada.";
    }

    @Override
    public String getNombreEstado() {
        return "ESPERANDO_PAGO";
    }
}
