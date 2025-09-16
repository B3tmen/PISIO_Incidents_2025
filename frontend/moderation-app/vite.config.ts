import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'
import { tanstackRouter } from '@tanstack/router-vite-plugin'
import tsconfigPaths from 'vite-tsconfig-paths';

// https://vite.dev/config/
export default defineConfig(({ command, mode }) => {
  // const env = loadEnv(mode, process.cwd(), '');
  // const backendAddress = JSON.stringify(env.VITE_BACKEND_ADDRESS);
  // const address = backendAddress ? backendAddress : "localhost"; 

  return ({
    plugins: [
      tanstackRouter({
        target: 'react',
        autoCodeSplitting: true,
        // you can add other options, e.g. routesDirectory or generatedRouteTree
      }),
      react(), 
      tsconfigPaths()
    ],
    server: {
      host: '0.0.0.0',  // so we can call localhost in the browser when app gets containerized with Docker, 
                        // otherwise it runs in the container's localhost
      port: 3000,       // Override 5173
      // proxy: {
      //   '/api': {
      //     target: `http://${address}:9002/api`,
      //     changeOrigin: true,
      //     rewrite: (path) => path.replace(/^\/api/, ''),
      //   },
      // }
    }
  });
});
