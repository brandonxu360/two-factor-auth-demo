import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import SecretMessage from '../components/SecretMessage';
import './DashboardPage.css';
import { toast } from 'sonner';
import { fetchCsrfToken } from '../utilities/fetchCsrfToken';

function DashboardPage() {
    const navigate = useNavigate();

    const handleSignOut = async () => {
        try {
            const res = await axios.post('http://localhost:8080/logout', {}, { withCredentials: true, withXSRFToken: true });

            if (res.status === 200) {
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

    return (
        <div className="dashboard-page">
            <button className="back-button" onClick={() => navigate(-1)}>Back</button>
            <h1>Dashboard</h1>
            <p>
                If you were successfully authenticated with two factors of authentication, <br />
                you should now be able to access the protected resources on this server. <br />
            </p>
            <p>
                Try revealing the secret message below!
            </p>

            <SecretMessage />

            <button className='sign-out-button' onClick={handleSignOut}>Sign out</button>
        </div>
    );
}

export default DashboardPage;
