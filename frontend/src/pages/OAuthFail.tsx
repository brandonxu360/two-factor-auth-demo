import { useEffect } from 'react';

/**
 * A page component that closes the oauth popup window when the user fails to authenticate.
 * @returns Null
 */
function OAuthFail() {
    useEffect(() => {
        window.close();
    }, []);

    return null;
}

export default OAuthFail;