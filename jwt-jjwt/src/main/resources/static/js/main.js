const tokenKey = 'jjwt.token';

async function login(event) {
    event.preventDefault();
    const message = document.querySelector('#message');
    message.textContent = 'Dang xac thuc...';
    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: document.querySelector('#email').value,
                password: document.querySelector('#password').value
            })
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Dang nhap that bai');
        localStorage.setItem(tokenKey, data.token);
        window.location.href = '/user/profile';
    } catch (error) {
        message.textContent = error.message;
        message.classList.add('error');
    }
}

async function loadProfile() {
    const token = localStorage.getItem(tokenKey);
    if (!token) { window.location.replace('/login'); return; }
    try {
        const response = await fetch('/users/me', { headers: { Authorization: `Bearer ${token}` } });
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Phien dang nhap khong hop le');
        document.querySelector('#full-name').textContent = data.fullName;
        document.querySelector('#profile-email').textContent = data.email;
        document.querySelector('#created-at').textContent = new Date(data.createdAt).toLocaleString('vi-VN');
    } catch (error) {
        localStorage.removeItem(tokenKey);
        document.querySelector('#message').textContent = error.message;
        setTimeout(() => window.location.replace('/login'), 1200);
    }
}

async function signup(event) {
    event.preventDefault();
    const message = document.querySelector('#message');
    const password = document.querySelector('#password').value;
    if (password !== document.querySelector('#confirm-password').value) {
        message.textContent = 'Mat khau xac nhan khong khop'; message.classList.add('error'); return;
    }
    try {
        const response = await fetch('/auth/signup', {method: 'POST', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({fullName: document.querySelector('#full-name').value, email: document.querySelector('#email').value, password})});
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Dang ky that bai');
        message.classList.remove('error'); message.textContent = 'Dang ky thanh cong';
        setTimeout(() => window.location.href = '/login', 800);
    } catch (error) { message.textContent = error.message; message.classList.add('error'); }
}

document.querySelector('#login-form')?.addEventListener('submit', login);
document.querySelector('#signup-form')?.addEventListener('submit', signup);
document.querySelector('#logout')?.addEventListener('click', () => {
    localStorage.removeItem(tokenKey);
    window.location.href = '/login';
});
if (document.body.dataset.page === 'profile') loadProfile();
