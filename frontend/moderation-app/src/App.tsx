import { createRouter, RouterProvider } from '@tanstack/react-router'
import './App.css'
import { Button, ConfigProvider } from 'antd'
import { routeTree } from 'routeTree.gen';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { GoogleOAuthProvider } from '@react-oauth/google'
import type { AxiosError } from 'axios';
import { SESSION_AUTH_TOKEN_KEY } from '@util/constants/stringConstants';


const router = createRouter({ routeTree });

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error: AxiosError) => {
        if (error.response?.status === 401) {
          return false; // Do not retry on 401, handle immediately
        }
        // Otherwise, use default retry behavior or custom logic
        return failureCount < 3; // Example: default 3 retries
      },
      onError: (error: AxiosError) => {
        if (error.response?.status === 401) {
          // Clear any stored session/token data
          localStorage.removeItem(SESSION_AUTH_TOKEN_KEY); // Example
          // Redirect to login page
          window.location.href = '/login'; // Or use TanStack Router's navigate
        }
      },
    },
  },
});

const CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID;

function App() {

  return (
    
    <ConfigProvider
      theme={{
        token: {
          
        }
      }}
    >
      <GoogleOAuthProvider clientId={CLIENT_ID}>
        <QueryClientProvider client={queryClient}>
          <RouterProvider router={router} />
        </QueryClientProvider>
      </GoogleOAuthProvider>
    </ConfigProvider>
  )
}

export default App
