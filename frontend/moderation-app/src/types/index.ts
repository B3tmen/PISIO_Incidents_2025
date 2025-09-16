export interface TabItem {
  key: string;
  label: string;
  icon?: React.ReactNode;
  children: React.ReactNode;
};

export interface IncidentTypeCardConfig {
  id: string;
  icon: React.ReactNode;
  title: string;
}

export interface Location {
  latitude: number;
  longitude: number;
  radius: number;
  address: string;
  city: string;
  state: string;
  country: string;
  zipCode: string;
}

export interface PageResponse<T> {
  content: T[];
  number: number;        
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Incident {
  id: number;
  type: string;
  subtype: string | null;
  status: string;
  reportedAt: Date;
  description: string;
  image: string;
  location: Location;
}

export interface ImageDTO {
  id: number;
  imageURL: string;
}

export interface IncidentModeration {
  id: number;
  incidentId: number;
  status: string;
  statusChangeTime: Date;
  moderatorId: number;
  images: ImageDTO[];
}

export interface UserDTO {
  email: string;
  givenName: string;
  familyName: string;
  picture: string;
}

export interface AuthenticationResponse {
  userDTO: UserDTO;
  accessToken: string;
  refreshToken: string;
}

export interface DailyCountProjection {
  reportedAt: Date;
  count: number;
}
export interface TypeCountProjection {
  type: string;
  count: number;
}
export interface LocationCountProjection {
  city: string;
  country: string;
  count: number;
}

