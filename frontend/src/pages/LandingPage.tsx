import { Link, useNavigate } from 'react-router-dom'
import './LandingPage.css'
import { fetchCsrfToken } from '../utilities/fetchCsrfToken';

/**
 * The landing page, where users can read about the application and
 * sign in with OAuth2 providers.
 * @returns Landing page component
 */
function LandingPage() {

    const navigate = useNavigate();

    /**
     * Redirects the user to the OAuth2 provider's sign-in page.
     * @param provider The OAuth2 provider to sign in with
     */
    function handleOAuthSignIn(provider: string) {
        const popup = window.open(`http://localhost:8080/oauth2/authorization/${provider}`, `${provider}-sign-in`, 'width=550,height=600');

        // Close the popup when the user is redirected back to the dashboard
        const checkPopup = setInterval(() => {
            try {
                if (popup?.window.location.href.includes('oauth2-success')) {
                    popup?.close()
                    fetchCsrfToken();
                    navigate('/dashboard');
                }
                else if (popup?.window.location.href.includes('oauth2-fail')) {
                    popup?.close()
                }
                if (!popup || !popup.closed) return;
                clearInterval(checkPopup);
            }
            catch {
                // console.error(error);
            }
            
        }, 100);
    }

    return (
        <div className="landing-page">
            <h1>Two-Factor Authentication Demo</h1>
            <p>
                Welcome to my two-factor authentication demo. This is a simple full-stack application <br />
                that protects the server resources with two factors of authentication - OAuth2 and OTP. <br />
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
