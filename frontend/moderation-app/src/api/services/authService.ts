import axiosInstance from "@api/axiosInstance";
import type { AuthenticationResponse } from "@types/index";
import { API_AUTH_GOOGLE_LOGIN_URL } from "@util/constants/apiUrls";

export const authService = {

  // Google OAuth Authentication
  googleLogin: async (token: string): Promise<AuthenticationResponse> => {
    const response = await axiosInstance.post<AuthenticationResponse>(API_AUTH_GOOGLE_LOGIN_URL, { token });
    return response.data;
  },

  refreshToken: async (refreshToken: string): Promise<string> => {
    const response = await axiosInstance.post<string>('/auth/refresh', { refreshToken });
    return response.data;
  },

  logout: async (): Promise<void> => {
    await axiosInstance.post('/auth/logout');
  }
}; 