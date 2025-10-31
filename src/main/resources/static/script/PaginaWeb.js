
const slides = document.querySelectorAll(".slide");
const dots = document.querySelectorAll(".dot");
console.log("Slides encontrados:", slides.length);
console.log("Dots encontrados:", dots.length);

document.addEventListener("DOMContentLoaded", () => {
  const slides = document.querySelectorAll(".slide");
  const next = document.getElementById("next");
  const prev = document.getElementById("prev");
  const indicators = document.getElementById("indicators");

  let index = 0;

  // Crear indicadores dinámicos
  slides.forEach((_, i) => {
    const dot = document.createElement("div");
    dot.classList.add("w-3", "h-3", "rounded-full", "cursor-pointer", "bg-gray-400", "transition");
    if (i === 0) dot.classList.add("bg-red-500");
    dot.addEventListener("click", () => showSlide(i));
    indicators.appendChild(dot);
  });

  const dots = indicators.querySelectorAll("div");

  // Función principal para mostrar slide
  function showSlide(i) {
    slides.forEach((s, j) => {
      s.classList.remove("active");
      dots[j].classList.remove("bg-red-500");
      dots[j].classList.add("bg-gray-400");
    });

    slides[i].classList.add("active");
    dots[i].classList.remove("bg-gray-400");
    dots[i].classList.add("bg-red-500");
    index = i;
  }

  // Botones
  next.addEventListener("click", () => {
    index = (index + 1) % slides.length;
    showSlide(index);
  });
	
  prev.addEventListener("click", () => {
    index = (index - 1 + slides.length) % slides.length;
    showSlide(index);
  });

  // Movimiento automático
  setInterval(() => {
    index = (index + 1) % slides.length;
    showSlide(index);
  }, 4000); // cambia cada 4 segundos
});






 /* const carousel = document.getElementById('carouselPeliculas');
  const prevBtn = document.getElementById('prevBtn');
  const nextBtn = document.getElementById('nextBtn');

  let index = 0;
  const slidesToShow = 3;
  const total = carousel.children.length;
  const maxIndex = Math.ceil(total / slidesToShow) - 1;

  function updateCarousel() {
    carousel.style.transform = `translateX(-${index * 100}%)`;
  }

  prevBtn.addEventListener('click', () => {
    index = index > 0 ? index - 1 : maxIndex;
    updateCarousel();
  });

  nextBtn.addEventListener('click', () => {
    index = index < maxIndex ? index + 1 : 0;
    updateCarousel();
  });

  // Auto-slide cada 3 segundos
  setInterval(() => {
    index = index < maxIndex ? index + 1 : 0;
    updateCarousel();
  }, 3000);
*/
