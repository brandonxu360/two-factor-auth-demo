import axios from 'axios';
import { toast } from 'sonner';

export const fetchCsrfToken = async () => {
    try {
        await axios.get('http://localhost:8080/csrf', { withCredentials: true });
    } catch (error) {
        console.error('Failed to fetch CSRF token:', error);
        toast.error('There was an issue fetching the  CSRF token');
    }
};
