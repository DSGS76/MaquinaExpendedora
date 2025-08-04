package com.discretas.maquinaexpendedora.models;

import com.discretas.maquinaexpendedora.state.EstadoMaquina;
import com.discretas.maquinaexpendedora.state.EstadoSeleccionando;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * Clase principal que representa la máquina expendedora.
 * Implementa el patrón State para manejar los diferentes estados de operación.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@Data
@Component
public class MaquinaExpendedora {

    /**
     * Estado actual de la máquina
     */
    private EstadoMaquina estadoActual;

    /**
     * Inventario de productos disponibles
     */
    private Map<String, Producto> inventario;

    /**
     * Transacción actual en proceso
     */
    private Transaccion transaccionActual;

    /**
     * Historial de transacciones realizadas
     */
    private List<Transaccion> historialTransacciones;

    /**
     * Dinero total disponible en la máquina para dar cambio
     */
    private double dineroDisponible;

    /**
     * Constructor por defecto
     */
    public MaquinaExpendedora() {
        this.estadoActual = new EstadoSeleccionando();
        this.inventario = new HashMap<>();
        this.historialTransacciones = new ArrayList<>();
        this.dineroDisponible = 50000.0; // Dinero inicial para cambio - cantidad más realista
    }

    /**
     * Inicializa el inventario con productos de ejemplo
     * @param productos Mapa de productos a cargar en el inventario
     */
    public void inicializarInventario(Map<String, Producto> productos) {
        this.inventario.putAll(productos);
    }

    /**
     * Cambia el estado de la máquina
     * @param nuevoEstado El nuevo estado a establecer
     */
    public void cambiarEstado(EstadoMaquina nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    /**
     * Selecciona un producto
     * @param codigoProducto Código del producto a seleccionar
     * @return Mensaje del resultado
     */
    public String seleccionarProducto(String codigoProducto) {
        return estadoActual.seleccionarProducto(this, codigoProducto);
    }

    /**
     * Inserta dinero en la máquina
     * @param monto Cantidad de dinero a insertar
     * @return Mensaje del resultado
     */
    public String insertarDinero(double monto) {
        return estadoActual.insertarDinero(this, monto);
    }

    /**
     * Confirma el pago
     * @return Mensaje del resultado
     */
    public String confirmarPago() {
        return estadoActual.confirmarPago(this);
    }

    /**
     * Dispensa el producto
     * @return Mensaje del resultado
     */
    public String dispensarProducto() {
        return estadoActual.dispensarProducto(this);
    }

    /**
     * Devuelve el cambio
     * @return Mensaje del resultado
     */
    public String devolverCambio() {
        return estadoActual.devolverCambio(this);
    }

    /**
     * Cancela la transacción actual
     * @return Mensaje del resultado
     */
    public String cancelarTransaccion() {
        return estadoActual.cancelarTransaccion(this);
    }

    /**
     * Crea una nueva transacción
     * @param producto Producto seleccionado
     * @param montoPagado Monto pagado por el cliente
     */
    public void crearTransaccion(Producto producto, double montoPagado) {
        String id = UUID.randomUUID().toString();
        this.transaccionActual = new Transaccion(id, producto, montoPagado);
    }

    /**
     * Finaliza la transacción actual
     */
    public void finalizarTransaccion() {
        if (transaccionActual != null) {
            historialTransacciones.add(transaccionActual);
            transaccionActual = null;
        }
    }

    /**
     * Obtiene el estado actual como string
     * @return Nombre del estado actual
     */
    public String getEstadoActualNombre() {
        return estadoActual.getNombreEstado();
    }

    /**
     * Verifica si hay suficiente cambio disponible
     * @param cambioRequerido Cantidad de cambio requerida
     * @return true si hay suficiente cambio
     */
    public boolean haySuficienteCambio(double cambioRequerido) {
        return dineroDisponible >= cambioRequerido;
    }

    /**
     * Reduce el dinero disponible por el cambio entregado
     * @param cambio Cantidad de cambio entregada
     */
    public void reducirDineroDisponible(double cambio) {
        if (dineroDisponible >= cambio) {
            dineroDisponible -= cambio;
        }
    }

    /**
     * Añade dinero a la máquina (solo el precio del producto, no el dinero insertado completo)
     * @param cantidad Cantidad a añadir
     */
    public void anyadirDinero(double cantidad) {
        dineroDisponible += cantidad;
    }

    /**
     * Añade el dinero insertado por el cliente a la máquina para dar cambio
     * @param dineroInsertado Dinero que insertó el cliente
     */
    public void anyadirDineroInsertado(double dineroInsertado) {
        dineroDisponible += dineroInsertado;
    }
}
