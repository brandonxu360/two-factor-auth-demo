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
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

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

const queryClient = new QueryClient()

createRoot(document.getElementById('root')!).render(

  //<StrictMode>
  <>
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <Toaster richColors theme='dark' />
        <RouterProvider router={router} />
      </UserProvider>
    </QueryClientProvider>
  </>
  //</StrictMode>,
)
