import type { Location } from ".";

export interface IncidentRequest {
    type: string;
    subtype?: string;
    location: Location;
    description: string;
}

export interface UpdateIncidentStatusRequest {
    status: string;
}

export interface TranslationRequest {
  text: string;
  sourceLang: string;
  targetLang: string;
}