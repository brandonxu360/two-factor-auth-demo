// import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import './index.css'
import LandingPage from './pages/LandingPage';
import DashboardPage from './pages/DashboardPage';
import { Toaster } from 'sonner';
import { fetchCsrfToken } from './utils/fetchCsrfToken';
import OAuthFail from './pages/OAuthFail';
import OAuthSuccess from './pages/OAuthSuccess';
import { UserProvider } from './context/UserProvider';
import TotpSetupPage from './pages/TotpSetupPage';

fetchCsrfToken();

const router = createBrowserRouter([
  {
    path: "/",
    element: <LandingPage />,
  },
  {
    path: "/dashboard",
    element: <DashboardPage />,
  },
  {
    path: "/oauth2-end",
    element: <OAuthFail />,
  },
  {
    path: "/oauth2-success",
    element: <OAuthSuccess />,
  },
  {
    path: "/totp-setup",
    element: <TotpSetupPage />,
  }
]);

createRoot(document.getElementById('root')!).render(

  //<StrictMode>
  <>
    <UserProvider>
      <Toaster richColors theme='dark' />
      <RouterProvider router={router} />
    </UserProvider>
  </>
  //</StrictMode>,
)
