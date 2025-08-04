/**
 * Frontend dinámico para la Máquina Expendedora
 * Maneja toda la interacción con la API REST sin recargar la página
 *
 * @author Duvan Gil
 * @version 1.0
 */

// Configuración de la API
const API_BASE = '/maquinaexpendedora/api/maquina';
const ENDPOINTS = {
    estado: `${API_BASE}/estado`,
    productos: `${API_BASE}/productos`,
    seleccionar: `${API_BASE}/seleccionar`,
    insertarDinero: `${API_BASE}/insertar-dinero`,
    confirmarPago: `${API_BASE}/confirmar-pago`,
    dispensar: `${API_BASE}/dispensar`,
    cancelar: `${API_BASE}/cancelar`,
    transaccionActual: `${API_BASE}/transaccion-actual`,
    historial: `${API_BASE}/historial`,
    reiniciar: `${API_BASE}/reiniciar`
};

// Estado de la aplicación
let estadoMaquina = {
    estado: 'SELECCIONANDO',
    productos: {},
    transaccionActual: null,
    dineroDisponible: 50000,
    historial: []
};

// Elementos del DOM
const elementos = {
    estadoActual: null,
    dineroDisponible: null,
    productosGrid: null,
    transaccionInfo: null,
    transaccionDetalles: null,
    displayPrincipal: null,
    botonesDinero: null,
    montoPersonalizado: null,
    insertarPersonalizado: null,
    confirmarPago: null,
    dispensarProducto: null,
    cancelarTransaccion: null
};

/**
 * Inicializa la aplicación cuando el DOM está cargado
 */
document.addEventListener('DOMContentLoaded', function() {
    inicializarElementos();
    inicializarEventos();
    cargarDatosIniciales();

    // Actualizar estado cada 2 segundos
    setInterval(actualizarEstado, 2000);
});

/**
 * Inicializa las referencias a elementos del DOM
 */
function inicializarElementos() {
    elementos.estadoActual = document.getElementById('estado-actual');
    elementos.dineroDisponible = document.getElementById('dinero-disponible');
    elementos.productosGrid = document.getElementById('productos-grid');
    elementos.transaccionInfo = document.getElementById('transaccion-info');
    elementos.transaccionDetalles = document.getElementById('transaccion-detalles');
    elementos.displayPrincipal = document.getElementById('display-principal');
    elementos.botonesDinero = document.querySelectorAll('.dinero-btn');
    elementos.montoPersonalizado = document.getElementById('montoPersonalizado');
    elementos.insertarPersonalizado = document.getElementById('insertar-personalizado');
    elementos.confirmarPago = document.getElementById('confirmar-pago');
    elementos.dispensarProducto = document.getElementById('dispensar-producto');
    elementos.cancelarTransaccion = document.getElementById('cancelar-transaccion');
}

/**
 * Inicializa los eventos de la interfaz
 */
function inicializarEventos() {
    // Botones de dinero predefinido
    elementos.botonesDinero.forEach(boton => {
        boton.addEventListener('click', function() {
            const monto = parseInt(this.dataset.monto);
            insertarDinero(monto);
        });
    });

    // Botón de insertar monto personalizado
    elementos.insertarPersonalizado?.addEventListener('click', function() {
        const monto = parseInt(elementos.montoPersonalizado.value);
        if (monto && monto >= 100) {
            insertarDinero(monto);
            elementos.montoPersonalizado.value = '';
        } else {
            mostrarMensaje('El monto mínimo es $100', 'error');
        }
    });

    // Enter en el input de monto personalizado
    elementos.montoPersonalizado?.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            elementos.insertarPersonalizado.click();
        }
    });

    // Botones de control
    elementos.confirmarPago?.addEventListener('click', confirmarPago);
    elementos.dispensarProducto?.addEventListener('click', dispensarProducto);
    elementos.cancelarTransaccion?.addEventListener('click', cancelarTransaccion);
}

/**
 * Carga los datos iniciales de la máquina
 */
async function cargarDatosIniciales() {
    try {
        await Promise.all([
            cargarProductos(),
            actualizarEstado(),
            cargarTransaccionActual(),
            actualizarDineroDisponible()
        ]);
    } catch (error) {
        console.error('Error cargando datos iniciales:', error);
        mostrarMensaje('Error al cargar los datos de la máquina', 'error');
    }
}

/**
 * Realiza una petición HTTP con manejo de errores
 */
async function realizarPeticion(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        const data = await response.json();

        if (!data.success) {
            throw new Error(data.message || 'Error en la operación');
        }

        return data;
    } catch (error) {
        console.error('Error en petición:', error);
        throw error;
    }
}

/**
 * Carga los productos disponibles
 */
async function cargarProductos() {
    try {
        const response = await realizarPeticion(ENDPOINTS.productos);
        estadoMaquina.productos = response.data;
        renderizarProductos();
    } catch (error) {
        mostrarMensaje('Error al cargar productos', 'error');
    }
}

/**
 * Actualiza el estado actual de la máquina
 */
async function actualizarEstado() {
    try {
        const response = await realizarPeticion(ENDPOINTS.estado);
        estadoMaquina.estado = response.data;
        actualizarInterfazEstado();
        // También actualizar dinero disponible cuando se actualiza el estado
        await actualizarDineroDisponible();
    } catch (error) {
        console.error('Error actualizando estado:', error);
    }
}

/**
 * Actualiza el dinero disponible en la máquina
 */
async function actualizarDineroDisponible() {
    try {
        const response = await realizarPeticion(`${API_BASE}/dinero-disponible`);
        estadoMaquina.dineroDisponible = response.data;
        mostrarDineroDisponible();
    } catch (error) {
        console.error('Error actualizando dinero disponible:', error);
    }
}

/**
 * Carga la transacción actual si existe
 */
async function cargarTransaccionActual() {
    try {
        const response = await realizarPeticion(ENDPOINTS.transaccionActual);
        estadoMaquina.transaccionActual = response.data;
        mostrarTransaccionActual();
    } catch (error) {
        // Es normal que no haya transacción actual
        estadoMaquina.transaccionActual = null;
        elementos.transaccionInfo.style.display = 'none';
    }
}

/**
 * Renderiza la grilla de productos
 */
function renderizarProductos() {
    if (!elementos.productosGrid) return;

    // Ordenar productos por código (A1, A2, A3, B1, B2, etc.)
    const productos = Object.values(estadoMaquina.productos).sort((a, b) => {
        // Extraer la letra y el número del código
        const aLetter = a.codigo.charAt(0);
        const aNumber = parseInt(a.codigo.substring(1));
        const bLetter = b.codigo.charAt(0);
        const bNumber = parseInt(b.codigo.substring(1));

        // Primero ordenar por letra, luego por número
        if (aLetter !== bLetter) {
            return aLetter.localeCompare(bLetter);
        }
        return aNumber - bNumber;
    });

    if (productos.length === 0) {
        elementos.productosGrid.innerHTML = `
            <div class="no-productos">
                <p>No hay productos disponibles</p>
            </div>
        `;
        return;
    }

    // Agrupar productos por categoría (letra del código)
    const categorias = {};
    productos.forEach(producto => {
        const categoria = producto.codigo.charAt(0);
        if (!categorias[categoria]) {
            categorias[categoria] = [];
        }
        categorias[categoria].push(producto);
    });

    // Mapeo de categorías a nombres descriptivos
    const nombresCategorias = {
        'A': '🥤 Bebidas Gaseosas',
        'B': '🌿 Bebidas Naturales',
        'C': '🍿 Snacks y Dulces',
        'D': '⭐ Productos Premium'
    };

    // Generar HTML organizado por categorías
    let htmlContent = '';

    Object.keys(categorias).sort().forEach(categoria => {
        const nombreCategoria = nombresCategorias[categoria] || `Categoría ${categoria}`;

        htmlContent += `
            <div class="categoria-header">
                <h3 class="categoria-titulo">${nombreCategoria}</h3>
            </div>
        `;

        categorias[categoria].forEach(producto => {
            const estaDisponible = producto.stock > 0;

            htmlContent += `
                <div class="producto-item ${!estaDisponible ? 'agotado' : ''}" 
                     data-codigo="${producto.codigo}"
                     data-categoria="${categoria}"
                     onclick="seleccionarProducto('${producto.codigo}')">
                    <div class="producto-codigo">${producto.codigo}</div>
                    <div class="producto-info">
                        <h4 class="producto-nombre">${producto.nombre}</h4>
                        <p class="producto-descripcion">${producto.descripcion}</p>
                        <div class="producto-detalles">
                            <span class="producto-precio">$${formatearNumero(producto.precio)}</span>
                            <span class="producto-stock ${producto.stock < 5 ? 'stock-bajo' : ''}">
                                Stock: ${producto.stock}
                            </span>
                        </div>
                    </div>
                    ${!estaDisponible ? '<div class="producto-agotado">AGOTADO</div>' : ''}
                </div>
            `;
        });
    });

    elementos.productosGrid.innerHTML = htmlContent;
}

/**
 * Selecciona un producto
 */
async function seleccionarProducto(codigoProducto) {
    try {
        mostrarCargando('Seleccionando producto...');

        const response = await realizarPeticion(`${ENDPOINTS.seleccionar}/${codigoProducto}`, {
            method: 'POST'
        });

        // Usar el mensaje del estado directamente y clasificar según el contenido
        const mensaje = response.data;
        const tipoNotificacion = clasificarMensaje(mensaje);
        mostrarMensaje(mensaje, tipoNotificacion);

        await Promise.all([
            actualizarEstado(),
            cargarTransaccionActual(),
            cargarProductos()
        ]);

    } catch (error) {
        mostrarMensaje(error.message, 'error');
    } finally {
        ocultarCargando();
    }
}

/**
 * Inserta dinero en la máquina
 */
async function insertarDinero(monto) {
    try {
        mostrarCargando('Insertando dinero...');

        const response = await realizarPeticion(`${ENDPOINTS.insertarDinero}/${monto}`, {
            method: 'POST'
        });

        // Usar el mensaje del estado directamente y clasificar según el contenido
        const mensaje = response.data;
        const tipoNotificacion = clasificarMensaje(mensaje);
        mostrarMensaje(mensaje, tipoNotificacion);

        await Promise.all([
            actualizarEstado(),
            cargarTransaccionActual()
        ]);

    } catch (error) {
        mostrarMensaje(error.message, 'error');
    } finally {
        ocultarCargando();
    }
}

/**
 * Confirma el pago de la transacción
 */
async function confirmarPago() {
    try {
        mostrarCargando('Confirmando pago...');

        const response = await realizarPeticion(ENDPOINTS.confirmarPago, {
            method: 'POST'
        });

        // Usar el mensaje del estado directamente y clasificar según el contenido
        const mensaje = response.data;
        const tipoNotificacion = clasificarMensaje(mensaje);
        mostrarMensaje(mensaje, tipoNotificacion);

        await Promise.all([
            actualizarEstado(),
            cargarTransaccionActual()
        ]);

    } catch (error) {
        mostrarMensaje(error.message, 'error');
    } finally {
        ocultarCargando();
    }
}

/**
 * Dispensa el producto
 */
async function dispensarProducto() {
    try {
        mostrarCargando('Dispensando producto...');

        const response = await realizarPeticion(ENDPOINTS.dispensar, {
            method: 'POST'
        });

        // Usar el mensaje del estado directamente y clasificar según el contenido
        const mensaje = response.data;
        const tipoNotificacion = clasificarMensaje(mensaje);
        mostrarMensaje(mensaje, tipoNotificacion);

        await Promise.all([
            actualizarEstado(),
            cargarTransaccionActual(),
            cargarProductos()
        ]);

    } catch (error) {
        mostrarMensaje(error.message, 'error');
    } finally {
        ocultarCargando();
    }
}

/**
 * Cancela la transacción actual
 */
async function cancelarTransaccion() {
    const confirmado = await mostrarConfirmacion(
        '¿Está seguro de que desea cancelar la transacción?',
        'Se devolverá el dinero insertado',
        'Cancelar Transacción',
        'Sí, cancelar',
        'No, continuar'
    );

    if (!confirmado) {
        return;
    }

    try {
        mostrarCargando('Cancelando transacción...');

        const response = await realizarPeticion(ENDPOINTS.cancelar, {
            method: 'POST'
        });

        // Usar el mensaje del estado directamente y clasificar según el contenido
        const mensaje = response.data;
        const tipoNotificacion = clasificarMensaje(mensaje);
        mostrarMensaje(mensaje, tipoNotificacion);

        await Promise.all([
            actualizarEstado(),
            cargarTransaccionActual(),
            cargarProductos()
        ]);

    } catch (error) {
        mostrarMensaje(error.message, 'error');
    } finally {
        ocultarCargando();
    }
}

/**
 * Clasifica el mensaje del backend para determinar el tipo de notificación
 * @param {string} mensaje - Mensaje proveniente del estado del backend
 * @returns {string} - Tipo de notificación (success, warning, info, error)
 */
function clasificarMensaje(mensaje) {
    const mensajeLower = mensaje.toLowerCase().trim();

    // Mensajes de error - tipo error (errores técnicos o del sistema)
    if (mensajeLower.startsWith('error:') ||
        mensajeLower.includes('error en') ||
        mensajeLower.includes('no encontrado') ||
        mensajeLower.includes('problemas técnicos') ||
        mensajeLower.includes('falla del sistema')) {
        return 'error';
    }

    // Mensajes de éxito - tipo success (operaciones completadas exitosamente)
    if (mensajeLower.includes('producto dispensado') ||
        mensajeLower.includes('gracias por su compra') ||
        mensajeLower.includes('pago confirmado') ||
        mensajeLower.includes('dispensando producto') ||
        (mensajeLower.includes('producto seleccionado') && mensajeLower.includes('inserte el dinero')) ||
        mensajeLower.includes('dinero adicional insertado') ||
        mensajeLower.includes('máquina reiniciada correctamente')) {
        return 'success';
    }

    // PRIORIDAD: Inserción de dinero normal (ESPERANDO_PAGO) - tipo info
    if (mensajeLower.includes('dinero insertado') &&
        (mensajeLower.includes('total:') || mensajeLower.includes('falta:') || mensajeLower.includes('presione confirmar'))) {
        return 'info';
    }

    // Mensajes de advertencia - tipo warning (situaciones que requieren atención del usuario)
    if (mensajeLower.includes('agotado') ||
        (mensajeLower.includes('no es suficiente') && mensajeLower.includes('pago')) ||
        mensajeLower.includes('debe insertar el dinero suficiente') ||
        mensajeLower.includes('debe confirmar el pago') ||
        mensajeLower.includes('debe completar el pago') ||
        mensajeLower.includes('ya hay un producto seleccionado') ||
        mensajeLower.includes('ya hay una transacción en proceso') ||
        mensajeLower.includes('no hay suficiente cambio disponible') ||
        mensajeLower.includes('no se puede cancelar') ||
        mensajeLower.includes('no puede insertar dinero') ||
        mensajeLower.includes('esperando que termine') ||
        mensajeLower.includes('siendo dispensado') ||
        mensajeLower.includes('complete la transacción o cancélela') ||
        mensajeLower.includes('primero debe seleccionar un producto')) {
        return 'warning';
    }

    // Mensajes informativos - tipo info (información neutral, estados, cancelaciones)
    if (mensajeLower.includes('transacción cancelada') ||
        mensajeLower.includes('dinero devuelto') ||
        mensajeLower.includes('no hay transacción para cancelar') ||
        mensajeLower.includes('no hay ninguna transacción') ||
        mensajeLower.includes('el cambio se devuelve automáticamente') ||
        mensajeLower.includes('no hay transacción en proceso') ||
        mensajeLower.includes('no hay producto seleccionado para dispensar')) {
        return 'info';
    }

    // Análisis contextual adicional para casos ambiguos

    // Si menciona cancelación por cualquier motivo, es info
    if (mensajeLower.includes('cancelad') &&
        (mensajeLower.includes('transacción') || mensajeLower.includes('dinero devuelto'))) {
        return 'info';
    }

    // Si menciona que debe hacer algo antes de otra acción, es warning
    if ((mensajeLower.includes('debe') || mensajeLower.includes('primero')) &&
        (mensajeLower.includes('antes') || mensajeLower.includes('primero'))) {
        return 'warning';
    }

    // Si menciona que algo ya está en proceso o ya existe, es warning
    if (mensajeLower.includes('ya ') &&
        (mensajeLower.includes('seleccionado') || mensajeLower.includes('proceso') || mensajeLower.includes('confirmado'))) {
        return 'warning';
    }

    // Mensajes informativos por defecto (incluye estados neutrales y navegación)
    return 'info';
}

/**
 * Actualiza la interfaz según el estado actual
 */
function actualizarInterfazEstado() {
    if (!elementos.estadoActual) return;

    // Actualizar badge de estado
    elementos.estadoActual.textContent = estadoMaquina.estado;
    elementos.estadoActual.className = `estado-badge estado-${estadoMaquina.estado.toLowerCase().replace('_', '-')}`;

    // ELIMINAMOS las restricciones de estado - todos los botones siempre habilitados
    // para que siempre se muestren las notificaciones correspondientes

    // Botones de dinero - siempre habilitados
    elementos.botonesDinero.forEach(btn => btn.disabled = false);
    if (elementos.montoPersonalizado) elementos.montoPersonalizado.disabled = false;
    if (elementos.insertarPersonalizado) elementos.insertarPersonalizado.disabled = false;

    // Botones de control - siempre habilitados
    if (elementos.confirmarPago) elementos.confirmarPago.disabled = false;
    if (elementos.dispensarProducto) elementos.dispensarProducto.disabled = false;
    if (elementos.cancelarTransaccion) elementos.cancelarTransaccion.disabled = false;

    // Productos - siempre seleccionables
    const productosItems = document.querySelectorAll('.producto-item');
    productosItems.forEach(item => {
        item.style.pointerEvents = 'auto';
        item.style.opacity = '1';
    });
}

/**
 * Muestra la información de la transacción actual
 */
function mostrarTransaccionActual() {
    if (!estadoMaquina.transaccionActual || !elementos.transaccionInfo) {
        if (elementos.transaccionInfo) elementos.transaccionInfo.style.display = 'none';
        return;
    }

    const t = estadoMaquina.transaccionActual;
    elementos.transaccionInfo.style.display = 'block';
    elementos.transaccionDetalles.innerHTML = `
        <div class="transaccion-detalle">
            <strong>Producto:</strong> ${t.producto.nombre} (${t.producto.codigo})
        </div>
        <div class="transaccion-detalle">
            <strong>Precio:</strong> $${formatearNumero(t.producto.precio)}
        </div>
        <div class="transaccion-detalle">
            <strong>Pagado:</strong> $${formatearNumero(t.montoPagado)}
        </div>
        ${t.montoPagado >= t.producto.precio ? `
            <div class="transaccion-detalle cambio-positivo">
                <strong>Cambio:</strong> $${formatearNumero(t.montoPagado - t.producto.precio)}
            </div>
        ` : `
            <div class="transaccion-detalle faltante">
                <strong>Falta:</strong> $${formatearNumero(t.producto.precio - t.montoPagado)}
            </div>
        `}
    `;
}

/**
 * Muestra un mensaje temporal en la interfaz con icono
 */
function mostrarMensaje(mensaje, tipo = 'info') {
    // Mapeo de iconos por tipo
    const iconos = {
        success: '✅',
        error: '❌',
        warning: '⚠️',
        info: 'ℹ️'
    };

    // Crear el elemento de mensaje
    const mensajeDiv = document.createElement('div');
    mensajeDiv.className = `mensaje-temporal mensaje-${tipo}`;
    mensajeDiv.innerHTML = `
        <div class="mensaje-contenido">
            <span class="mensaje-icono">${iconos[tipo] || iconos.info}</span>
            <span class="mensaje-texto">${mensaje}</span>
            <button class="mensaje-cerrar" onclick="cerrarMensaje(this)">×</button>
            <div class="mensaje-progress"></div>
        </div>
    `;

    // Agregar al body
    document.body.appendChild(mensajeDiv);

    // Animar entrada
    setTimeout(() => {
        mensajeDiv.classList.add('mostrar');
    }, 100);

    // Auto-eliminar después de 4 segundos con animación de salida
    setTimeout(() => {
        if (mensajeDiv.parentElement) {
            mensajeDiv.classList.remove('mostrar');
            setTimeout(() => {
                if (mensajeDiv.parentElement) {
                    mensajeDiv.remove();
                }
            }, 400);
        }
    }, 4000);
}

/**
 * Cierra un mensaje manualmente
 */
function cerrarMensaje(boton) {
    const mensajeDiv = boton.closest('.mensaje-temporal');
    if (mensajeDiv) {
        mensajeDiv.classList.remove('mostrar');
        setTimeout(() => {
            if (mensajeDiv.parentElement) {
                mensajeDiv.remove();
            }
        }, 400);
    }
}

/**
 * Muestra indicador de carga
 */
function mostrarCargando(mensaje = 'Procesando...') {
    let cargandoDiv = document.getElementById('cargando-overlay');

    if (!cargandoDiv) {
        cargandoDiv = document.createElement('div');
        cargandoDiv.id = 'cargando-overlay';
        cargandoDiv.className = 'cargando-overlay';
        document.body.appendChild(cargandoDiv);
    }

    cargandoDiv.innerHTML = `
        <div class="cargando-contenido">
            <div class="spinner"></div>
            <p>${mensaje}</p>
        </div>
    `;

    cargandoDiv.style.display = 'flex';
}

/**
 * Oculta el indicador de carga
 */
function ocultarCargando() {
    const cargandoDiv = document.getElementById('cargando-overlay');
    if (cargandoDiv) {
        cargandoDiv.style.display = 'none';
    }
}

/**
 * Formatea números con separadores de miles
 */
function formatearNumero(numero) {
    return numero.toLocaleString('es-CO');
}

/**
 * Muestra el dinero disponible en el display
 */
function mostrarDineroDisponible() {
    if (elementos.dineroDisponible) {
        elementos.dineroDisponible.textContent = formatearNumero(estadoMaquina.dineroDisponible);
    }
}

/**
 * Muestra un pop-up de confirmación personalizado
 * @param {string} titulo - Título del pop-up
 * @param {string} mensaje - Mensaje descriptivo
 * @param {string} encabezado - Encabezado del modal
 * @param {string} textoConfirmar - Texto del botón de confirmación
 * @param {string} textoCancelar - Texto del botón de cancelación
 * @returns {Promise<boolean>} - Promesa que resuelve true si se confirma, false si se cancela
 */
function mostrarConfirmacion(titulo, mensaje = '', encabezado = 'Confirmación', textoConfirmar = 'Confirmar', textoCancelar = 'Cancelar') {
    return new Promise((resolve) => {
        // Crear el modal de confirmación
        const modalDiv = document.createElement('div');
        modalDiv.className = 'modal-confirmacion-overlay';
        modalDiv.innerHTML = `
            <div class="modal-confirmacion">
                <div class="modal-header">
                    <h3 class="modal-titulo">${encabezado}</h3>
                </div>
                <div class="modal-contenido">
                    <div class="modal-icono">⚠️</div>
                    <div class="modal-texto">
                        <h4>${titulo}</h4>
                        ${mensaje ? `<p>${mensaje}</p>` : ''}
                    </div>
                </div>
                <div class="modal-botones">
                    <button class="btn-modal btn-cancelar" data-accion="cancelar">
                        <span class="icono-btn">✕</span>
                        ${textoCancelar}
                    </button>
                    <button class="btn-modal btn-confirmar" data-accion="confirmar">
                        <span class="icono-btn">✓</span>
                        ${textoConfirmar}
                    </button>
                </div>
            </div>
        `;

        // Agregar al body
        document.body.appendChild(modalDiv);

        // Función para cerrar el modal
        function cerrarModal(confirmado) {
            modalDiv.classList.add('cerrando');
            setTimeout(() => {
                if (modalDiv.parentElement) {
                    modalDiv.remove();
                }
                resolve(confirmado);
            }, 300);
        }

        // Event listeners para los botones
        const btnConfirmar = modalDiv.querySelector('[data-accion="confirmar"]');
        const btnCancelar = modalDiv.querySelector('[data-accion="cancelar"]');

        btnConfirmar.addEventListener('click', () => cerrarModal(true));
        btnCancelar.addEventListener('click', () => cerrarModal(false));

        // Cerrar con escape
        function handleKeydown(e) {
            if (e.key === 'Escape') {
                document.removeEventListener('keydown', handleKeydown);
                cerrarModal(false);
            }
        }
        document.addEventListener('keydown', handleKeydown);

        // Cerrar al hacer clic fuera del modal
        modalDiv.addEventListener('click', (e) => {
            if (e.target === modalDiv) {
                cerrarModal(false);
            }
        });

        // Animar entrada
        setTimeout(() => {
            modalDiv.classList.add('mostrar');
        }, 10);

        // Enfocar el botón de cancelar por defecto
        setTimeout(() => {
            btnCancelar.focus();
        }, 100);
    });
}

// Funciones para el historial (si estamos en la página de historial)
if (window.location.pathname.includes('historial')) {
    /**
     * Carga y actualiza el historial dinámicamente
     */
    async function cargarHistorial() {
        try {
            const response = await realizarPeticion(ENDPOINTS.historial);
            estadoMaquina.historial = response.data;
            renderizarHistorial();
        } catch (error) {
            mostrarMensaje('Error al cargar el historial', 'error');
        }
    }

    /**
     * Renderiza el historial de transacciones
     */
    function renderizarHistorial() {
        // Esta función se puede implementar si se quiere hacer el historial completamente dinámico
        // Por ahora, el historial se carga desde el servidor en la carga inicial de la página
    }

    // Auto-actualizar historial cada 10 segundos
    setInterval(cargarHistorial, 10000);
}
