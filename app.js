const toast = document.querySelector('#toast');
let toastTimer;

function showToast(message) {
  toast.textContent = message;
  toast.classList.add('visible');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove('visible'), 3200);
}

document.querySelector('#new-project').addEventListener('click', () => {
  showToast('Project workspace ready — connect your workflow to create the first project.');
});

document.querySelector('#review-decisions').addEventListener('click', () => {
  showToast('Two leadership decisions are ready for review.');
});

document.querySelector('#search').addEventListener('input', (event) => {
  const query = event.target.value.trim().toLowerCase();
  document.querySelectorAll('.project-row, .activity-list li').forEach((item) => {
    item.style.opacity = !query || item.textContent.toLowerCase().includes(query) ? '1' : '.28';
  });
});

document.querySelector('.menu-button').addEventListener('click', () => {
  showToast('Navigation is optimized for desktop in this starter dashboard.');
});

