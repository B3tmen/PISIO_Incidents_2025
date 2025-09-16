import axiosInstance from "@api/axiosInstance";
import type { TranslationRequest } from "@types/requests";
import { API_TRANSLATION_URL } from "@util/constants/apiUrls";

export const translationService = {
    translateText: async (textSource: string, source: string, target: string) => {
        try {
            const translationRequest: TranslationRequest = {
                text: textSource,
                sourceLang: source,
                targetLang: target
            }
            const resp = await axiosInstance.post(API_TRANSLATION_URL, translationRequest);

            return resp.data;
        } 
        catch (error: any) {
            throw new Error("Translation failed, check your source and target languages.");
        }
        
    }
}