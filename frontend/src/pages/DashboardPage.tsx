import SecretMessage from '../components/SecretMessage';
import './DashboardPage.css'

function DashboardPage() {

    return (
        <div className="dashboard-page">
            <h1>Dashboard</h1>
            <p>
                If you were successfully authenticated with two factors of authentication, <br />
                you should now be able to access the protected resources on this server. <br />
            </p>
            <p>
                Try revealing the secret message below!
            </p>

            <SecretMessage />

            <button className='sign-out-button' onClick={() => { }}>Sign out</button>

        </div>
    )
}

export default DashboardPage;
