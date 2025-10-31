window.addEventListener('DOMContentLoaded', () => {
    const fechaInput = document.getElementById("fecha");
    const horaInicioInput = document.getElementById("horaInicio");
    const horaFinInput = document.getElementById("horaFin");

    // Duración de la película desde data-attribute
    const duracionPelicula = parseInt(horaInicioInput.dataset.duracion) || 0;

    // ---- Configurar fecha mínima: hoy ----
    const hoy = new Date();
    const yyyy = hoy.getFullYear();
    const mm = String(hoy.getMonth() + 1).padStart(2, '0');
    const dd = String(hoy.getDate()).padStart(2, '0');
    fechaInput.setAttribute('min', `${yyyy}-${mm}-${dd}`);

    // ---- Inicializar horaFin si hay horaInicio ----
    if (horaInicioInput.value) {
        actualizarHoraFin(horaInicioInput.value);
    }

    // ---- Evento: cambio de horaInicio ----
    horaInicioInput.addEventListener("input", () => {
        actualizarHoraFin(horaInicioInput.value);
        validarHoraInicio();
    });

    // ---- Evento: cambio de fecha ----
    fechaInput.addEventListener("input", validarFecha);

    // ---- Validación formulario antes de enviar ----
    const form = document.querySelector("form");
    form.addEventListener("submit", (e) => {
        let valid = true;

        if (!validarFecha()) valid = false;
        if (!validarHoraInicio()) valid = false;

        if (!valid) {
            e.preventDefault(); // Evita envío
            e.stopPropagation();
        }
    });

    // ---- Función: actualizar horaFin automáticamente ----
    function actualizarHoraFin(horaInicio) {
        const partes = horaInicio.split(":");
        if (partes.length === 2) {
            const date = new Date();
            date.setHours(parseInt(partes[0]));
            date.setMinutes(parseInt(partes[1]));
            date.setMinutes(date.getMinutes() + duracionPelicula);

            const hh = String(date.getHours()).padStart(2, "0");
            const mm = String(date.getMinutes()).padStart(2, "0");
            horaFinInput.value = `${hh}:${mm}`;
        } else {
            horaFinInput.value = "";
        }
    }

    // ---- Validar Fecha ----
    function validarFecha() {
        const fechaSeleccionada = new Date(fechaInput.value);
        const fechaHoy = new Date();
        fechaHoy.setHours(0,0,0,0); // Ignorar hora

        if (fechaSeleccionada < fechaHoy) {
            mostrarError(fechaInput, "La fecha no puede ser menor a hoy");
            return false;
        } else {
            limpiarError(fechaInput);
            return true;
        }
    }

    // ---- Validar Hora Inicio ----
    function validarHoraInicio() {
        if (!horaInicioInput.value) {
            mostrarError(horaInicioInput, "Debe seleccionar una hora de inicio");
            return false;
        }
        limpiarError(horaInicioInput);
        return true;
    }

    // ---- Mostrar error ----
    function mostrarError(input, mensaje) {
        const feedback = input.nextElementSibling;
        if(feedback) {
            feedback.textContent = mensaje;
            input.classList.add("is-invalid");
        }
    }

    // ---- Limpiar error ----
    function limpiarError(input) {
        const feedback = input.nextElementSibling;
        if(feedback) {
            feedback.textContent = "";
            input.classList.remove("is-invalid");
        }
    }
});
