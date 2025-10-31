document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('form');

  // ---- Inputs
  const campos = [
    { input: document.getElementById('titulo'), error: document.getElementById('errorTitulo'), minLength: 1 },
    { input: document.getElementById('descripcion'), error: document.getElementById('errorDescripcion'), minLength: 10 },
    { input: document.getElementById('duracion'), error: document.getElementById('errorDuracion'), minValue: 30 },
    { input: document.getElementById('idioma'), error: document.getElementById('errorIdioma'), minLength: 1 },
    { input: document.getElementById('clasificacion'), error: document.getElementById('errorClasificacion') },
    { input: document.getElementById('generos'), error: document.getElementById('errorGeneros'), multiSelect: true },
    { input: document.getElementById('fechaEstreno'), error: document.getElementById('errorFechaEstreno') },
    { input: document.getElementById('precio'), error: document.getElementById('errorPrecio'), minValue: 0.01 },
    { input: document.getElementById('estado'), error: document.getElementById('errorEstado') },
    { input: document.getElementById('imagen'), error: document.getElementById('errorImagen'), file: true }
  ];

  // ---- Helpers
  const setInvalid = (input, errorDiv, msg) => {
    input.classList.remove('is-valid');
    input.classList.add('is-invalid');
    if (errorDiv) errorDiv.textContent = msg || '';
    removeCheck(input);
    return false;
  };

  const setValid = (input, errorDiv) => {
    input.classList.remove('is-invalid');
    input.classList.add('is-valid');
    if (errorDiv) errorDiv.textContent = '';
    addCheck(input);
    return true;
  };


  // ---- Validaciones específicas
  const validarCampo = (campo) => {
    const { input, error, minLength, minValue, multiSelect, file } = campo;
    if (!input) return true;

    if (file) {
      if (!input.files || input.files.length === 0) return setValid(input, error);
      const f = input.files[0];
      if (!/\.(jpg|jpeg|png)$/i.test(f.name)) return setInvalid(input, error, 'Solo JPG/JPEG/PNG.');
      if (f.size > 3 * 1024 * 1024) return setInvalid(input, error, 'Máx 3 MB.');
      return setValid(input, error);
    }

    if (multiSelect) {
      const selected = [...input.options].filter(opt => opt.selected).length;
      return selected === 0 ? setInvalid(input, error, 'Selecciona al menos un género.') : setValid(input, error);
    }

    if (minLength && input.value.trim().length < minLength) {
      return setInvalid(input, error, `Mínimo ${minLength} caracteres.`);
    }

    if (minValue && parseFloat(input.value) < minValue) {
      return setInvalid(input, error, `Valor mínimo ${minValue}.`);
    }

    return setValid(input, error);
  };

  // ---- Eventos en tiempo real
  campos.forEach(campo => {
    if (campo.input) {
      const evento = campo.input.tagName === 'SELECT' || campo.multiSelect ? 'change' : 'input';
      campo.input.addEventListener(evento, () => validarCampo(campo));
    }
  });

  // ---- Submit
  form.addEventListener('submit', e => {
    let ok = true;
    campos.forEach(campo => {
      ok = validarCampo(campo) && ok;
    });

    if (!ok) {
      e.preventDefault();
      const firstInvalid = document.querySelector('.is-invalid');
      if (firstInvalid) firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
      return;
    }

    const btn = form.querySelector('button[type="submit"]');
    if (btn) { btn.disabled = true; btn.innerText = 'Enviando...'; }
  });
});
