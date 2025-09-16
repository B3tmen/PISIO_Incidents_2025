import axiosInstance from "@api/axiosInstance"
import type { AxiosResponse } from "axios";

export const getAll = async <T>(url: string): Promise<T[]> => {
    const response = await axiosInstance.get(url);
    
    return response.data;
}

export const insert = async <T>(url: string, data: T): Promise<T> => {
    const response = await axiosInstance.post(url, data);

    return response.data;
}

export const insertFormData = async(url: string, formData: FormData): Promise<AxiosResponse> => {
    const response = await axiosInstance.post(url, formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    });

    return response;
} 