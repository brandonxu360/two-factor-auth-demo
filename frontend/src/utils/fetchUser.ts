// src/utilities/userApi.ts
import axios from 'axios';

export const fetchUser = async () => {
    try {
        const response = await axios.get('http://localhost:8080/user', {
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch user info:', error);
        return null;
    }
};