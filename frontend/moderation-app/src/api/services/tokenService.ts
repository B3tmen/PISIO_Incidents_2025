const ACCESS_TOKEN_KEY = 'accessToken';

export const tokenService = {
  // Access token methods
  setAccessToken: (token: string) => {
    sessionStorage.setItem(ACCESS_TOKEN_KEY, token);
  },
  
  getAccessToken: (): string | null => {
    return sessionStorage.getItem(ACCESS_TOKEN_KEY);
  },
  
  removeAccessToken: () => {
    sessionStorage.removeItem(ACCESS_TOKEN_KEY);
  },
  
  // Check if we have an access token
  hasAccessToken: (): boolean => {
    return !!sessionStorage.getItem(ACCESS_TOKEN_KEY);
  }
};