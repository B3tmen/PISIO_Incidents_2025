import type { Incident } from "@types/index";

export function isIncident(obj: unknown): obj is Incident {
  return (
    typeof obj === 'object' &&
    obj !== null &&
    'location' in obj &&
    'description' in obj
  );
}