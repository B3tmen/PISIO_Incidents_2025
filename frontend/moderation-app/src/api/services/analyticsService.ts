import { API_ANALYTICS_URL } from "@util/constants/apiUrls"
import { getAll } from "./apiService";
import type { DailyCountProjection, LocationCountProjection, TypeCountProjection } from "@types/index";

export const analyticsService = {

    getByType: async () => {
        const url = API_ANALYTICS_URL + "/by-type";
        const response = await getAll<TypeCountProjection>(url);

        return response;
    },

    getByDay: async () => {
        const url = API_ANALYTICS_URL + "/by-day";
        const response = await getAll<DailyCountProjection>(url);

        return response;
    },

    getByLocation: async () => {
        const url = API_ANALYTICS_URL + "/by-location";
        const response = await getAll<LocationCountProjection>(url);

        return response;
    },
}