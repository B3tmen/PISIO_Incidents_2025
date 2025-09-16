
// TS disables for type-safety since TS 5.8, "erasableSyntax" rule
// export enum IncidentStatus {
//   FIRE, FLOOD, CRIME, ACCIDENT
// }

// enums
export const IncidentType = {
    FIRE: "FIRE",
    FLOOD: "FLOOD",
    CRIME: "CRIME",
    ACCIDENT: "ACCIDENT"
} as const;
type IncidentType = typeof IncidentType[keyof typeof IncidentType];

export const IncidentSubtype = {
    // fire subtypes
    ARSON: "ARSON",  FOREST_FIRE: "FOREST_FIRE",

    // crime subtypes
    CAR_ROBBERY: "CAR_ROBBERY", BANK_ROBBERY: "BANK_ROBBERY", ASSAULT: "ASSAULT",

    // accident subtypes
    CAR_ACCIDENT: "CAR_ACCIDENT", ELECTRICAL_ACCIDENT: "ELECTRICAL_ACCIDENT", CONSTRUCTION_ACCIDENT: "CONSTRUCTION_ACCIDENT"
} as const;
type IncidentSubtype = typeof IncidentSubtype[keyof typeof IncidentSubtype];

export const IncidentStatus = {
    REPORTED: "REPORTED", 
    PENDING: "PENDING", 
    APPROVED: "APPROVED", 
    REJECTED: "REJECTED", 
    RESOLVED: "RESOLVED", 
    DUPLICATE: "DUPLICATE", 
    CANCELLED: "CANCELLED"

} as const;
type IncidentStatus = typeof IncidentStatus[keyof typeof IncidentStatus];