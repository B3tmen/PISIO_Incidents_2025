import axiosInstance from "@api/axiosInstance"
import type { UpdateIncidentStatusRequest } from "@types/requests"
import { API_MODERATION_BASE_URL } from "@util/constants/apiUrls"
import type { AxiosResponse } from "axios"

export const moderationService = {

    updateIncidentStatus: async (incidentId: number, updateStatusRequest: UpdateIncidentStatusRequest): Promise<AxiosResponse> => {
        const url = API_MODERATION_BASE_URL + `/${incidentId}` + "/status"
        const response = axiosInstance.put(url, updateStatusRequest);

        return response;
    }
}
