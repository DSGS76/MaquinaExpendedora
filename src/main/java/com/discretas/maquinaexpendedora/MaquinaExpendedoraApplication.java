package com.discretas.maquinaexpendedora;

import com.discretas.maquinaexpendedora.models.MaquinaExpendedora;
import com.discretas.maquinaexpendedora.models.Producto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase principal de la aplicación Spring Boot para la Máquina Expendedora.
 * Esta aplicación proporciona servicios REST aplicando patrón State
 * para manejar los estados de la máquina expendedora.
 *
 * @author Duvan Gil
 * @version 1.0
 */
@SpringBootApplication
public class MaquinaExpendedoraApplication {

    /**
     * Se insertan productos directamente en el main como prueba.
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MaquinaExpendedoraApplication.class, args);

        // Obtener la instancia de la máquina expendedora del contexto de Spring
        MaquinaExpendedora maquina = context.getBean(MaquinaExpendedora.class);

        // Inicializar productos directamente en el main
        Map<String, Producto> productos = new HashMap<>();

        // Bebidas gaseosas (Serie A)
        productos.put("A1", new Producto("A1", "Coca Cola", 2500.0, 10, "Bebida gaseosa 350ml"));
        productos.put("A2", new Producto("A2", "Pepsi", 2500.0, 8, "Bebida gaseosa 350ml"));
        productos.put("A3", new Producto("A3", "Sprite", 2300.0, 12, "Bebida gaseosa limón 350ml"));

        // Bebidas naturales (Serie B)
        productos.put("B1", new Producto("B1", "Agua", 1500.0, 15, "Agua natural 500ml"));
        productos.put("B2", new Producto("B2", "Jugo Naranja", 3000.0, 5, "Jugo natural 300ml"));
        productos.put("B3", new Producto("B3", "Té Helado", 2800.0, 7, "Té frío sabor limón 400ml"));

        // Snacks y dulces (Serie C)
        productos.put("C1", new Producto("C1", "Snickers", 3500.0, 12, "Barra de chocolate con maní"));
        productos.put("C2", new Producto("C2", "Papitas", 2000.0, 20, "Papitas fritas naturales 45g"));
        productos.put("C3", new Producto("C3", "Oreo", 2200.0, 15, "Galletas chocolate 154g"));

        // Productos premium (Serie D)
        productos.put("D1", new Producto("D1", "Red Bull", 4500.0, 6, "Bebida energética 250ml"));
        productos.put("D2", new Producto("D2", "Pringles", 4000.0, 8, "Papitas premium 124g"));
        productos.put("D3", new Producto("D3", "Kit Kat", 3200.0, 10, "Barra de chocolate wafer"));

        // Cargar productos en la máquina
        maquina.inicializarInventario(productos);

    }
}
