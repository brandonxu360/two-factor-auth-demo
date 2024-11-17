import { useEffect, useState } from 'react';
import './TotpSetupPage.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/useUser';

function TotpSetupPage() {
    const { refreshUser } = useUser();
    const [qrCodeUrl, setQrCodeUrl] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [otp, setOtp] = useState('');
    const [success, setSuccess] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchQrCode = async () => {
            try {
                const response = await axios.get('http://localhost:8080/totp-setup', { withCredentials: true, responseType: 'blob' });
                const url = URL.createObjectURL(response.data);
                setQrCodeUrl(url);
            } catch {
                setError('Failed to fetch QR code. Please try again.');
            }
        };

        fetchQrCode();
    }, []);

    const handleVerify = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/totp-verify', 
                { code: otp }, 
                { withCredentials: true, withXSRFToken: true }
            );
            
            if (response.status === 200 && response.data === true) {
                refreshUser();
                setSuccess('TOTP setup complete. You will be redirected to the dashboard.');
                setTimeout(() => {
                    navigate('/dashboard');
                }, 2000);
            }
        } catch {
            setError('TOTP verification failed. Please try again.');
        }
    };

    return (
        <div className="totp-setup-page">
            <h2>Set Up Two-Factor Authentication</h2>
            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}
            {qrCodeUrl ? (
                <div>
                    <p>Scan the QR code below with your authenticator app:</p>
                    <img src={qrCodeUrl} alt="QR Code" />
                </div>
            ) : (
                <p>Loading QR code...</p>
            )}
            <form onSubmit={handleVerify}>
                <input
                    type="text"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    placeholder="Enter OTP"
                    required
                />
                <button type="submit">Verify</button>
            </form>
        </div>
    )
}

export default TotpSetupPage;