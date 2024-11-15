
import { useState } from 'react';
import './SecretMessage.css';
import axios from 'axios';

function SecretMessage() {
    const [revealed, setRevealed] = useState(false);
    const [secret, setSecret] = useState('');

    const fetchSecret = async () => {
        try {
            const response = await axios.get('http://localhost:8080/secret-message', { withCredentials: true });
            setSecret(response.data);
        } catch (error) {
            console.error('Error fetching the secret message:', error);
            setRevealed(false);
        }
    };

    const handleReveal = () => {
        if (!revealed) {
            fetchSecret();
        }
        setRevealed(!revealed);
    };

    return (
        <div className="secret-message-card">
            <button onClick={handleReveal}>
                {revealed ? 'Hide Secret' : 'Reveal Secret'}
            </button>
            {revealed && <p className="secret-message">{secret}</p>}
        </div>
    );
}

export default SecretMessage;