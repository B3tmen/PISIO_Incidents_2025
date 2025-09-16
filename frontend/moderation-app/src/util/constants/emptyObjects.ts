import type { Location } from "@types/index";
import type { IncidentRequest } from "@types/requests";

export const EMPTY_LOCATION: Location = {
    latitude: 0,
    longitude: 0,
    radius: 0,
    address: '',
    city: '',
    state: '',
    country: '',
    zipCode: ''
}

export const EMPTY_INCIDENT_REQUEST: IncidentRequest = {
    type: '',
    subtype: '',
    location: EMPTY_LOCATION,
    description: ''
}