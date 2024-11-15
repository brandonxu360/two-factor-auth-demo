import SecretMessage from '../components/SecretMessage';
import './DashboardPage.css'

function DashboardPage() {

    return (
        <div className="dashboard-page">
            <h1>Awesome, you've been authenticated!</h1>
            <p>
                You've successfully authenticated with two factors of authentication. <br />
                You can now access the protected resources on this server. <br />
            </p>

            <SecretMessage />

            <button className='sign-out-button' onClick={() => { }}>Sign out</button>

        </div>
    )
}

export default DashboardPage;
