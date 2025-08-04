package com.discretas.maquinaexpendedora.state;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Transaccion;

/**
 * Estado donde se dispensa el producto y se entrega el cambio.
 *
 * @author Duvan Gil
 * @version 1.0
 */
public class EstadoDispensando implements EstadoMaquina {

    @Override
    public String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto) {
        return "La máquina está dispensando un producto. Espere a que termine la operación.";
    }

    @Override
    public String insertarDinero(MaquinaExpendedora maquina, double monto) {
        return "No puede insertar dinero mientras se dispensa un producto.";
    }

    @Override
    public String confirmarPago(MaquinaExpendedora maquina) {
        return "El pago ya fue confirmado. Producto siendo dispensado.";
    }

    @Override
    public String dispensarProducto(MaquinaExpendedora maquina) {
        Transaccion transaccion = maquina.getTransaccionActual();
        if (transaccion == null) {
            maquina.cambiarEstado(new EstadoSeleccionando());
            return "Error: No hay transacción activa.";
        }

        // Reducir stock del producto
        transaccion.getProducto().reducirStock();

        // Añadir dinero insertado por el cliente a la máquina
        maquina.anyadirDineroInsertado(transaccion.getMontoPagado());

        // Marcar transacción como completada
        transaccion.setEstado(Transaccion.EstadoTransaccion.COMPLETADA);

        String mensaje = "Producto dispensado: " + transaccion.getProducto().getNombre();

        // Verificar si hay cambio que devolver y reducir el dinero disponible
        if (transaccion.getCambio() > 0) {
            mensaje += ". Su cambio: $" + (int) transaccion.getCambio();
            maquina.reducirDineroDisponible(transaccion.getCambio());
        }

        // Finalizar transacción y volver al estado inicial
        maquina.finalizarTransaccion();
        maquina.cambiarEstado(new EstadoSeleccionando());

        return mensaje + ". Gracias por su compra.";
    }

    @Override
    public String devolverCambio(MaquinaExpendedora maquina) {
        return "El cambio se devuelve automáticamente con el producto.";
    }

    @Override
    public String cancelarTransaccion(MaquinaExpendedora maquina) {
        return "No se puede cancelar la transacción durante la dispensación.";
    }

    @Override
    public String getNombreEstado() {
        return "DISPENSANDO";
    }
}
