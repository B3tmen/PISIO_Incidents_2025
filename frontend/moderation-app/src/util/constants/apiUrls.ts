const address = "localhost"; //backendAddressDocker ? backendAddressDocker : "localhost";
const port = 9002;
const version = "v1";
export const API_BASE_URL = `http://${address}:${port}/api/${version}`;

export const API_AUTH_GOOGLE_LOGIN_URL = API_BASE_URL + "/auth/google/login";
export const API_AUTH_REFRESH_URL = API_BASE_URL + "/auth/refresh";

export const API_INCIDENTS_URL = API_BASE_URL + "/incidents";

export const API_MODERATION_BASE_URL = API_BASE_URL + "/moderation";
export const API_MODERATION_PENDING_INCIDENTS_URL = API_MODERATION_BASE_URL + "/incidents";

export const API_NOMINATIM_REVERSE_GEOCODE_URL = "https://nominatim.openstreetmap.org/reverse?format=json&accept-language=sr-Latn&";

export const API_ANALYTICS_URL = API_BASE_URL + "/analytics";

export const API_TRANSLATION_URL = API_INCIDENTS_URL + "/translation";
