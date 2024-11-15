import axios from 'axios';

export const fetchCsrfToken = async () => {
    try {
        await axios.get('http://localhost:8080/csrf', { withCredentials: true });
    } catch (error) {
        console.error('Failed to fetch CSRF token:', error);
    }
};
