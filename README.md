# 🎰 Máquina Expendedora - Estado Finito

Implementación completa de una máquina expendedora utilizando el patrón de diseño State (Estado Finito) desarrollado con Spring Boot y una interfaz web moderna e interactiva.

## 📋 Descripción

Esta aplicación web simula una máquina expendedora real con diferentes estados que maneja:
- Selección de productos con códigos alfanuméricos
- Inserción de dinero con validaciones
- Procesamiento y confirmación de pagos
- Dispensación de productos con control de stock
- Devolución de cambio automática
- Cancelación de transacciones en cualquier momento

## ✨ Características

- ✅ **Patrón State completo** con 5 estados diferentes
- ✅ **Interfaz realista** que simula una máquina expendedora real
- ✅ **Sistema de productos** organizado por categorías (A, B, C, D)
- ✅ **Control de stock** en tiempo real
- ✅ **Manejo de dinero** con cambio automático
- ✅ **Historial de transacciones** con estadísticas
- ✅ **API REST** para todas las operaciones
- ✅ **Interfaz responsiva** con efectos modernos

## 🎯 Estados de la Máquina

### 1. **SELECCIONANDO**
Estado inicial donde el usuario puede seleccionar productos

### 2. **ESPERANDO_PAGO**
Producto seleccionado, esperando inserción de dinero

### 3. **PROCESANDO_PAGO**
Dinero suficiente insertado, esperando confirmación

### 4. **DISPENSANDO**
Dispensando producto y devolviendo cambio

### 5. **SIN_CAMBIO**
Estado de error cuando no hay cambio disponible

## 🛠️ Tecnologías

- **Backend:** Spring Boot 3.5.4, Maven, Java 24
- **Frontend:** Thymeleaf, JavaScript ES6, CSS3, HTML5
- **Arquitectura:** MVC, REST API, Patrón State
- **Diseño:** Glassmorphism, Animaciones CSS, Responsive Design

## 📦 Instalación

### Prerrequisitos
- Java 24 o superior
- Maven 4.0.0+
- Git

### Opción 1: Con IntelliJ IDEA (Recomendado)
*Proyecto desarrollado originalmente en IntelliJ IDEA*

1. **Clonar el repositorio**
```bash
git clone https://github.com/DSGS76/MaquinaExpendedora.git
```

2. **Abrir en IntelliJ IDEA**
    - Abre IntelliJ IDEA
    - File → Open → Selecciona la carpeta del proyecto
    - El IDE detectará automáticamente Maven y configurará el proyecto

3. **Ejecutar la aplicación**
    - Ejecuta la clase `MaquinaExpendedoraApplication`
    - O usa el botón de Run en la interfaz

### Opción 2: Instalación General

1. **Clonar el repositorio**
```bash
git clone https://github.com/DSGS76/MaquinaExpendedora.git
cd MaquinaExpendedora
```

2. **Instalar dependencias**
```bash
mvn clean install
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

### Acceso a la aplicación
Una vez ejecutada la aplicación, accede a:
```
http://localhost:5000/maquinaexpendedora
```

## 🎯 Uso

### Interfaz Web

1. **Seleccionar producto**
    - Navega por las categorías de productos (A, B, C, D)
    - Haz clic en cualquier producto disponible
    - El sistema cambiará al estado ESPERANDO_PAGO

2. **Insertar dinero**
    - Usa los botones predefinidos ($500, $1.000, $2.000, $5.000)
    - O ingresa un monto personalizado (mínimo $100)
    - El sistema valida si es suficiente para el producto

3. **Confirmar pago**
    - Una vez insertado dinero suficiente, presiona "Confirmar Pago"
    - El sistema verifica disponibilidad de cambio

4. **Dispensar producto**
    - Presiona "Dispensar" para obtener tu producto
    - El cambio se devuelve automáticamente
    - El stock se actualiza en tiempo real

5. **Ver historial**
    - Accede al historial de todas las transacciones
    - Visualiza estadísticas de ventas

### Productos Disponibles

#### 🥤 Bebidas Gaseosas (Serie A)
- **A1**: Coca Cola - $2.500 (350ml)
- **A2**: Pepsi - $2.500 (350ml)
- **A3**: Sprite - $2.300 (350ml)

#### 🌿 Bebidas Naturales (Serie B)
- **B1**: Agua - $1.500 (500ml)
- **B2**: Jugo Naranja - $3.000 (300ml)
- **B3**: Té Helado - $2.800 (400ml)

#### 🍿 Snacks y Dulces (Serie C)
- **C1**: Snickers - $3.500 (Barra con maní)
- **C2**: Papitas - $2.000 (45g)
- **C3**: Oreo - $2.200 (154g)

#### ⭐ Productos Premium (Serie D)
- **D1**: Red Bull - $4.500 (250ml)
- **D2**: Pringles - $4.000 (124g)
- **D3**: Kit Kat - $3.200 (Wafer)

## 🏗️ Estructura del Proyecto

```
src/main/
├── java/com/discretas/maquinaexpendedora/
│   ├── models/              # MaquinaExpendedora, Producto, Transaccion
│   ├── state/               # Estados del patrón State de MaquinaExpendedora
│   ├── services/            # MaquinaService
│   ├── presentation/        # Controllers y DTOs
│   │   ├── controller/      # REST y View Controllers
│   │   └── dto/            # ApiResponseDTO
│   └── utils/              # Constants
└── resources/
    ├── static/             # CSS y JavaScript
    │   ├── css/           # main.css, maquina.css
    │   └── js/            # maquina.js, header.js
    └── templates/          # Plantillas Thymeleaf
        ├── fragments/      # Layout components
        └── maquina/       # historial.html
```

## 🔗 API Endpoints

### Base URL
```
http://localhost:5000/maquinaexpendedora/api/maquina
```

### Endpoints Disponibles

#### 1. Obtener estado actual
```http
GET /estado
```

#### 2. Obtener productos disponibles
```http
GET /productos
```

#### 3. Seleccionar producto
```http
POST /seleccionar/{codigoProducto}
```

#### 4. Insertar dinero
```http
POST /insertar-dinero/{monto}
```

#### 5. Confirmar pago
```http
POST /confirmar-pago
```

#### 6. Dispensar producto
```http
POST /dispensar
```

#### 7. Cancelar transacción
```http
POST /cancelar
```

#### 8. Obtener transacción actual
```http
GET /transaccion-actual
```

#### 9. Obtener historial
```http
GET /historial
```

#### 10. Obtener dinero disponible
```http
GET /dinero-disponible
```

### Ejemplo de Respuesta API
```json
{
  "data": "Producto seleccionado: Coca Cola - Precio: $2500. Inserte el dinero.",
  "message": "OPERACION EXITOSA",
  "success": true,
  "status": 200,
  "timestamp": "2025-01-03T10:30:00"
}
```

## 🎲 Patrón State (Estado Finito)

### Implementación del Patrón

La máquina expendedora utiliza el patrón State para manejar sus diferentes comportamientos según el estado actual:

```java
public interface EstadoMaquina {
    String seleccionarProducto(MaquinaExpendedora maquina, String codigoProducto);
    String insertarDinero(MaquinaExpendedora maquina, double monto);
    String confirmarPago(MaquinaExpendedora maquina);
    String dispensarProducto(MaquinaExpendedora maquina);
    String cancelarTransaccion(MaquinaExpendedora maquina);
    String getNombreEstado();
}
```

### Transiciones de Estado

```
SELECCIONANDO → ESPERANDO_PAGO → PROCESANDO_PAGO → DISPENSANDO
     ↑              ↓                  ↓                ↓
     ←─────────── CANCELADO ←──────────┘                ↓
     ↑                                                  ↓
     ←─────────── SIN_CAMBIO (Error)                    ↓
     ↑                                                  ↓
     ←──────────────────────────────────────────────────┘
```

### Ventajas del Patrón State
- ✅ **Separación de responsabilidades** por estado
- ✅ **Fácil mantenimiento** y extensión
- ✅ **Comportamiento predecible** en cada estado
- ✅ **Transiciones controladas** entre estados
- ✅ **Código limpio** y organizado

## 💰 Sistema de Dinero

### Características
- **Dinero inicial**: $50.000 para cambio
- **Denominaciones**: $500, $1.000, $2.000, $5.000, o monto personalizado
- **Monto mínimo**: $100
- **Cambio automático**: Se calcula y devuelve automáticamente
- **Validación**: Verifica disponibilidad de cambio antes de procesar

### Flujo de Dinero
1. Cliente inserta dinero → Se acumula en la transacción
2. Al confirmar pago → Se verifica cambio disponible
3. Al dispensar → Dinero del cliente se añade a la máquina
4. Cambio se resta del dinero disponible

## 📊 Sistema de Transacciones

### Estados de Transacción
- **EN_PROCESO**: Transacción iniciada pero no completada
- **COMPLETADA**: Producto dispensado exitosamente
- **CANCELADA**: Transacción cancelada por el usuario
- **ERROR**: Error en el procesamiento

### Información de Transacción
- ID único generado automáticamente
- Producto seleccionado con detalles
- Monto pagado y cambio devuelto
- Fecha y hora exacta
- Estado actual de la transacción

## 🎨 Características de UI/UX

- **Diseño de máquina real** con elementos decorativos (botones, ranura)
- **Display LCD simulado** con efectos retro y animaciones
- **Panel de control lateral** con botones de dinero estilo real
- **Grid de productos** organizado por categorías
- **Estados visuales** con badges de colores y animaciones
- **Notificaciones inteligentes** que se clasifican automáticamente
- **Efectos glassmorphism** y gradientes modernos
- **Responsive design** para todos los dispositivos
- **Scrollbars personalizadas** con tema de la aplicación

## 🧪 Casos de Uso

### Caso 1: Compra Exitosa
1. Usuario selecciona "Coca Cola" (A1 - $2.500)
2. Inserta $3.000
3. Confirma el pago
4. Recibe el producto y $500 de cambio

### Caso 2: Dinero Insuficiente
1. Usuario selecciona "Red Bull" (D1 - $4.500)
2. Inserta $2.000
3. Sistema indica que faltan $2.500
4. Usuario puede insertar más dinero o cancelar

### Caso 3: Sin Cambio Disponible
1. Usuario selecciona producto de $1.500
2. Inserta $50.000 (requiere $48.500 de cambio)
3. Sistema detecta insuficiente cambio disponible
4. Transacción se cancela automáticamente

### Caso 4: Producto Agotado
1. Usuario intenta seleccionar producto sin stock
2. Sistema muestra "Producto agotado"
3. Usuario debe seleccionar otro producto

## 🔧 Configuración

### Configuración Inicial
- **Dinero disponible**: $50.000
- **Productos**: 12 productos en 4 categorías

## 📈 Estadísticas y Métricas

### Historial de Transacciones
- Total de transacciones realizadas
- Cantidad de transacciones completadas vs canceladas
- Total de dinero vendido
- Cambio total devuelto

### Métricas en Tiempo Real
- Stock actual de cada producto
- Dinero disponible para cambio
- Estado actual de la máquina
- Transacción en curso (si existe)

## 🔒 Validaciones y Seguridad

### Validaciones de Entrada
- Montos mínimos y máximos
- Códigos de productos válidos
- Estados permitidos para cada operación
- Disponibilidad de stock y cambio

### Manejo de Errores
- Respuestas estructuradas con códigos HTTP apropiados
- Mensajes descriptivos para el usuario
- Logging de operaciones importantes
- Recuperación automática de estados inconsistentes

## 🎓 Conceptos de Matemáticas Discretas

### Estados Finitos
La máquina implementa un **autómata finito determinista** donde:
- **Q** = {SELECCIONANDO, ESPERANDO_PAGO, PROCESANDO_PAGO, DISPENSANDO, SIN_CAMBIO}
- **Σ** = {seleccionar, insertar, confirmar, dispensar, cancelar}
- **δ** = Función de transición implementada en cada estado
- **q₀** = SELECCIONANDO (estado inicial)
- **F** = {SELECCIONANDO} (estado de aceptación)

### Transiciones Deterministas
Cada estado tiene transiciones bien definidas y deterministas, garantizando comportamiento predecible.

## 👨‍💻 Autor

**Duvan Gil** - [GitHub](https://github.com/DSGS76)

## 🙏 Agradecimientos

- Patrón State por Gang of Four (Design Patterns)
- Spring Boot Team por el excelente framework
- Comunidad de desarrolladores por las mejores prácticas
- Matemáticas Discretas por los conceptos de autómatas finitos

---

⭐ Si este proyecto te fue útil para entender el patrón State y autómatas finitos, ¡no olvides darle una estrella!
