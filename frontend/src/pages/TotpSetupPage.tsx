import { useState } from 'react';
import './TotpSetupPage.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/useUser';
import { useQuery, useMutation } from '@tanstack/react-query';
import { toast } from 'sonner';

function TotpSetupPage() {
    const { refreshUser } = useUser();
    const [otp, setOtp] = useState('');
    const navigate = useNavigate();

    // Query for fetching QR code
    const { data: qrCodeUrl, isError } = useQuery({
        queryKey: ['totpQrCode'],
        queryFn: async () => {
            const response = await axios.get('http://localhost:8080/totp-setup',
                { withCredentials: true, responseType: 'blob' }
            );
            return URL.createObjectURL(response.data);
        }
    });

    // Mutation for verifying TOTP
    const verifyMutation = useMutation({
        mutationFn: async (code: string) => {
            return axios.post('http://localhost:8080/totp-verify-setup',
                { code },
                { withCredentials: true, withXSRFToken: true }
            );
        },
        onSuccess: (response) => {
            if (response.status === 200 && response.data === true) {
                refreshUser();
                toast.success('TOTP setup complete. You will be redirected to the dashboard.');
                setTimeout(() => {
                    navigate('/dashboard', { replace: true });
                }, 2000);
            }
            else {
                toast.error('Verification failed. Please try again.');
            }
        },
        onError: (error) => {
            console.error('Error verifying TOTP:', error);
            toast.error('Something went wrong. Please try again.');
        }
    });

    const handleVerify = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        verifyMutation.mutate(otp);
    };

    if (isError) {
        return <div className="error-message">Failed to fetch QR code. Please try again.</div>;
    }

    return (
        <div className="totp-setup-page">
            <div className="totp-setup-container">
                <h1>Set Up Two-Factor Authentication</h1>

                <div className="qr-code-container">
                    <p>Scan the QR code below with your authenticator app:</p>
                    {qrCodeUrl ? <img src={qrCodeUrl} alt="QR Code" /> : <div className="loading">Loading...</div>}
                </div>

                <form onSubmit={handleVerify}>
                    <input
                        className="totp-input"
                        type="text"
                        value={otp}
                        onChange={(e) => setOtp(e.target.value)}
                        placeholder="Enter verification code"
                        maxLength={6}
                        required
                    />
                    <button
                        className="verify-button"
                        type="submit"
                        disabled={verifyMutation.isPending}
                    >
                        Verify
                    </button>
                </form>
            </div>
        </div>
    );
}

export default TotpSetupPage;