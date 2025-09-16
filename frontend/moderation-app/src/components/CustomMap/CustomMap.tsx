import React from "react";
import { type LatLngExpression } from "leaflet";
import { MapContainer, Marker, Popup, TileLayer, Circle, useMapEvents } from "react-leaflet";
import type { Incident, Location } from "@types/index";
import { isIncident } from "@util/typeCheckers";


interface Props<T extends { location: Location }> {
    centerCoordinates: LatLngExpression;
    dataToDisplay?: T[];
    onClick?: (e: any) => void;
    manualMarkerPosition?: LatLngExpression | null;
}

function ClickHandler({ onClick }: { onClick: (e: any) => void }) {
    useMapEvents({
        click(e) {
            onClick(e);
        },
    });
    return null;
}

function CustomMap<T extends { location: Location }>({ 
    centerCoordinates, 
    dataToDisplay, 
    onClick,
    manualMarkerPosition
}: Props<T>) {

    return(
        <MapContainer 
            center={centerCoordinates} 
            zoom={13} 
            scrollWheelZoom={true} 
            style={{ height: '100vh', width: '100%' }}
        >
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            {onClick && <ClickHandler onClick={onClick} />}

            {/* Manual marker */}
            {manualMarkerPosition && (
                <Marker position={manualMarkerPosition}>
                    <Popup>
                        Selected Location
                    </Popup>
                </Marker>
            )}

            {/* Automatic location marker — only if not manual AND userCoordinates exists */}
            {!manualMarkerPosition && Array.isArray(centerCoordinates) && (
                <Marker position={centerCoordinates}>
                    <Popup>Current Location</Popup>
                </Marker>
            )}
            
            {/* Render circles + markers for all data items that have a location */}
            {dataToDisplay?.map((item, idx) => {
                let incident: Incident | null = null;
                if(isIncident(item)) {
                    incident = item;
                }

                if (!item.location) return null;
                const { latitude, longitude, radius, address, city } = item.location;
                const position: LatLngExpression = [latitude, longitude];
                
                return (
                    <React.Fragment key={idx}>
                        <Marker position={position}>
                            <Popup>
                                {address}, {city}, status: {incident?.status}
                            </Popup>
                        </Marker>
                    
                        <Circle 
                            center={position} 
                            radius={radius} 
                            pathOptions={{ color: '#e74c3c', fillOpacity: 0.2 }}
                        />
                    </React.Fragment>
                );
                })
            }
        </MapContainer>
    );
}

export default CustomMap;