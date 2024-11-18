import { Link, useNavigate } from 'react-router-dom'
import './LandingPage.css'
import { fetchCsrfToken } from '../utils/fetchCsrfToken';
import { fetchUser } from '../utils/fetchUser';
import { useUser } from '../context/useUser';
import { toast } from 'sonner';

/**
 * The landing page, where users can read about the application and
 * sign in with OAuth2 providers.
 * @returns Landing page component
 */
function LandingPage() {
    const { setUser } = useUser();
    const navigate = useNavigate();

    /**
     * Redirects the user to the OAuth2 provider's sign-in page in a popup window. 
     * Then closes the popup and determines where to redirect the user based on the result.
     * @param provider The OAuth2 provider to sign in with
     */
    function handleOAuthSignIn(provider: string) {
        const popup = window.open(`http://localhost:8080/oauth2/authorization/${provider}`, `${provider}-sign-in`, 'width=550,height=600');

        // Close the popup when the user is redirected back to the dashboard
        const checkPopup = setInterval(async () => {
            try {
                if (!popup || popup.closed) {
                    clearInterval(checkPopup);
                    return;
                }
    
                const popupUrl = popup.location.href;
    
                if (popupUrl.includes('oauth2-success')) {
                    popup.close();
    
                    const urlParams = new URLSearchParams(new URL(popupUrl).search);
                    const needs2FA = urlParams.get('needs2FA') === 'true';
    
                    if (needs2FA) {
                        // Redirect to the 2FA page
                        navigate('/totp-verification');
                    } else {
                        // Fetch user details and navigate to the dashboard
                        await fetchCsrfToken();
                        const userData = await fetchUser();
                        setUser(userData);
                        navigate('/dashboard');
                        toast.success('Signed in successfully');
                    }
                } else if (popupUrl.includes('oauth2-fail')) {
                    popup.close();
                    toast.error('OAuth sign-in failed');
                }
            } catch {
                // Popup location access error; ignore
            }

        }, 100);
    }

    return (
        <div className="landing-page">
            <h1>Two-Factor Authentication Demo</h1>
            <p>
                Welcome to my two-factor authentication demo. This is a simple full-stack application <br />
                that protects the server resources with two factors of authentication - OAuth2 and TOTP. <br />
            </p>

            <div className='button-row'>
                <button onClick={() => handleOAuthSignIn("google")}>Sign in with Google</button>
                <button onClick={() => handleOAuthSignIn("github")}>Sign in with GitHub</button>
            </div>

            <p>
                You can try to access the protected resources on the server without being authenticated <br />
                by navigating directly to the <Link to="/dashboard">Dashboard</Link> page. Normally, this route would also be <br />
                protected on the frontend, but for the sake of this demo, it is not.
            </p>
        </div>
    )
}

export default LandingPage
