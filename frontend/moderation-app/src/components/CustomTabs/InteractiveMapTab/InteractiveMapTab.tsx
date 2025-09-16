import type { LatLngExpression } from "leaflet";
import { useQuery } from "@tanstack/react-query";

import CustomMap from "@components/CustomMap/CustomMap";
import type { Incident } from "@types/index";
import { getAll } from "@api/services/apiService";
import { API_INCIDENTS_URL } from "@util/constants/apiUrls";
import 'leaflet/dist/leaflet.css'
import { IncidentStatus } from "@types/enums";
import { Flex } from "antd";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import IncidentFilters from "@components/IncidentFilter/IncidentFilter";
import { faFilter } from "@fortawesome/free-solid-svg-icons";
import { useMemo, useState } from "react";
import { normalizeCity } from "@components/IncidentFilter/filterService";

function InteractiveMapTab() {
    const coordinatesBL: LatLngExpression = [44.772182, 17.191000];
    const [filters, setFilters] = useState({
        time: 'All',
        type: 'All',
        city: 'All'
    });

    const { data: incidentsData, isLoading, error } = useQuery({
        queryKey: ['incidentsInteractiveMap'],
        queryFn: async () => getAll<Incident>(API_INCIDENTS_URL),
    })

    // if (isLoading) return 'Loading...';
    // if (error) return 'Error loading data 2!';

    const filteredIncidents = useMemo(() => {
        if (!incidentsData) return [];
        const now = new Date();

        return incidentsData.filter(incident => {
            // time filter
            if (incident.reportedAt) {
            const diffDays = (now.getTime() - new Date(incident.reportedAt).getTime()) / (1000 * 60 * 60 * 24);
                if (filters.time === '24h' && diffDays > 1) return false;
                if (filters.time === '7d' && diffDays > 7) return false;
                if (filters.time === '31d' && diffDays > 31) return false;
            }

            // type/subtype filter
            if (filters.type !== 'All') {
                if (incident.type !== filters.type && incident.subtype !== filters.type) return false;
            }

            // city filter
            if (filters.city !== 'All') {
                if (normalizeCity(incident.location.city) !== normalizeCity(filters.city)) return false;
            }

            return true;
        });
    }, [incidentsData, filters]);

    return (
        <Flex gap={10} vertical>
            {/* Filter section */}
            <Flex gap={10} align="center">
                <FontAwesomeIcon icon={faFilter} style={{ color: 'var(--color-gray)' }} />
                <IncidentFilters 
                    incidentsData={incidentsData!}
                    onChange={setFilters} 
                />
            </Flex>

            <CustomMap<Incident> 
                centerCoordinates={coordinatesBL}
                dataToDisplay={filteredIncidents?.filter(data => data.status === IncidentStatus.PENDING || data.status === IncidentStatus.REPORTED)}
            />
        </Flex>
    );
}

export default InteractiveMapTab;