import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import './index.css'
import LandingPage from './pages/LandingPage';
import DashboardPage from './pages/DashboardPage';
import { Toaster } from 'sonner';
import { fetchCsrfToken } from './utilities/fetchCsrfToken';

const router = createBrowserRouter([
  {
    path: "/",
    element: <LandingPage />,
  },
  {
    path: "/dashboard",
    element: <DashboardPage />,
  }
]);

fetchCsrfToken();

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Toaster richColors theme='dark'/>
    <RouterProvider router={router} />
  </StrictMode>,
)
