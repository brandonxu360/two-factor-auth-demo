import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import SecretMessage from '../components/SecretMessage';
import './DashboardPage.css';
import { toast } from 'sonner';
import { fetchCsrfToken } from '../utils/fetchCsrfToken';
import { useUser } from '../context/useUser';
import { useMutation } from '@tanstack/react-query';

function DashboardPage() {
    const { user, setUser, refreshUser } = useUser();
    const navigate = useNavigate();

    const handleSignOut = async () => {
        try {
            const res = await axios.post('http://localhost:8080/logout', {}, { withCredentials: true, withXSRFToken: true });

            if (res.status === 200) {
                setUser(null);
                toast.success('Signed out successfully');
                // Refresh the CSRF token after signing out
                fetchCsrfToken();
                navigate("/");
            }
        } catch (error) {
            console.error('Error signing out:', error);

            if (axios.isAxiosError(error) && error.response?.status === 401) {
                toast.error('You must be logged in to sign out');
            }
            else {
                toast.error('Error signing out');
            }
        }
    };

    const disableTotpMutation = useMutation({
        mutationFn: async () => {
            return axios.post('http://localhost:8080/totp-disable', 
                {},
                { withCredentials: true, withXSRFToken: true }
            );
        },
        onSuccess: () => {
            toast.success('Two-factor authentication disabled');
            refreshUser();
        },
        onError: () => {
            toast.error('Failed to disable two-factor authentication');
        }
    });

    const handleTotpButton = () => {
        if (user?.twoFactorEnabled) {
            disableTotpMutation.mutate();
        } else {
            navigate('/totp-setup');
        }
    };

    console.log(user);
    return (
        <div className="dashboard-page">
            <button className="back-button" onClick={() => navigate(-1)}>Back</button>
            <h1>Dashboard</h1>

            {/* Display user information */}
            {user ? (
                <div className="user-info">
                    {user.imageUrl && <img src={user.imageUrl} alt="Profile" className="profile-picture" />}
                    <div className="user-details">
                        <p><strong>Id:</strong> {user.id}</p>
                        <p><strong>Username:</strong> {user.name}</p>
                        <p><strong>Provider:</strong> {user.provider}</p>
                        <p><strong>Two-factor enabled:</strong> {String(user.twoFactorEnabled)}</p>
                    </div>
                </div>
            ) : (
                <p>User information not available. Please log in.</p>
            )}

            <p>
                Welcome to the dashboard! If you were successfully authenticated, you <br />
                should now be able to access the protected resources on this server. <br />
            </p>
            <p>
                Try revealing the secret message below!
            </p>

            <SecretMessage />
            <div className='button-column'>
                <button 
                    onClick={handleTotpButton} 
                    disabled={!user || disableTotpMutation.isPending}
                >
                    {user?.twoFactorEnabled ? 'Disable Two-Factor Authentication' : 'Enable Two-Factor Authentication'}
                </button>
                <button onClick={handleSignOut} disabled={!user}>Sign out</button>
            </div>
        </div>
    );
}

export default DashboardPage;
