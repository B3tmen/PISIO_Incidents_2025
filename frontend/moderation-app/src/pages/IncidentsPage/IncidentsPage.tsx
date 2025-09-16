import { useMemo, useState } from "react";
import { Flex, FloatButton, message, Select, Spin, Switch, Tooltip, Typography, type UploadFile } from "antd";
import { PlusOutlined } from '@ant-design/icons';
import type { LatLngExpression } from "leaflet";
import 'leaflet/dist/leaflet.css';

import CustomMap from "@components/CustomMap/CustomMap";
import AddIncidentModal from "@components/AddIncidentModal/AddIncidentModal";
import { API_INCIDENTS_URL, API_NOMINATIM_REVERSE_GEOCODE_URL } from "@util/constants/apiUrls";
import { EMPTY_LOCATION } from "@util/constants/emptyObjects";
import type { Incident, Location } from "@types/index";
import type { IncidentRequest } from "@types/requests";
import { getAll, insertFormData } from "@api/services/apiService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFilter } from "@fortawesome/free-solid-svg-icons";
import { useQuery } from "@tanstack/react-query";
import { IncidentStatus } from "@types/enums";
import IncidentFilters from "@components/IncidentFilter/IncidentFilter";
import { normalizeCity } from "@components/IncidentFilter/filterService";


function IncidentsPage() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [switchValue, setSwitchValue] = useState(false);
    const [userCoordinates, setUserCoordinates] = useState<LatLngExpression | null>(null);
    const [manualLocation, setManualLocation] = useState<Location | null>(null);
    const [autoLocation, setAutoLocation] = useState<Location | null>(null);
    const [isLoadingLocation, setIsLoadingLocation] = useState(false);
    const [messageApi, contextHolder] = message.useMessage();
    const [isUploading, setIsUploading] = useState(false);
    const [filters, setFilters] = useState({
        time: 'All',
        type: 'All',
        city: 'All'
    });
    
    const coordinatesBL: LatLngExpression = [44.772182, 17.191000];

    const { data: incidentsData } = useQuery({
        queryKey: ['incidents'],
        queryFn: async () => getAll<Incident>(API_INCIDENTS_URL), //API_MODERATION_PENDING_INCIDENTS_URL
    });

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

    const reverseGeocode = async (lat: number, lng: number): Promise<Location> => {
        try {
            const response = await fetch(
                `${API_NOMINATIM_REVERSE_GEOCODE_URL}lat=${lat}&lon=${lng}&zoom=18&addressdetails=1`
            );
            const data = await response.json();

            //console.log("Incidents: ", incidentsData);

            return {
                latitude: lat,
                longitude: lng,
                radius: 100,
                address: data.address?.road + (data.address?.house_number ? ' ' + data.address.house_number : '') || '',
                city: data.address?.city || data.address?.town || data.address?.village || '',
                state: data.address?.state || '',
                country: data.address?.country || '',
                zipCode: data.address?.postcode || ''
            };
        } catch (error) {
            console.error("Reverse geocoding error:", error);
            return {
                latitude: lat,
                longitude: lng,
                radius: 100,
                address: '',
                city: '',
                state: '',
                country: '',
                zipCode: ''
            };
        }
    };

    const handleLocationToggle = (checked: boolean) => {
        setSwitchValue(checked);

        if (checked) {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    async (position) => {
                        const { latitude, longitude } = position.coords;
                        setUserCoordinates([latitude, longitude]);
                        setIsLoadingLocation(true);

                        const location = await reverseGeocode(latitude, longitude);
                        setAutoLocation(location);
                        setIsLoadingLocation(false);
                    },
                    (error) => {
                        console.error("Geolocation error:", error);
                    },
                    {
                        enableHighAccuracy: true,
                        timeout: 5000,
                        maximumAge: 0
                    }
                );
            } else {
                console.error("Geolocation not supported by this browser.");
            }
        } else {
            setUserCoordinates(null);
            setAutoLocation(null);
        }
    };

    const handleMapClick = async (e: any) => {
        const { lat, lng } = e.latlng;
        setIsLoadingLocation(true);
        const location = await reverseGeocode(lat, lng);
        setManualLocation(location);
        setIsLoadingLocation(false);
    };

    const getCurrentCenter = () => {
        if (manualLocation) return [manualLocation.latitude, manualLocation.longitude] as LatLngExpression;
        if (userCoordinates) return userCoordinates;
        return coordinatesBL;
    };

    const onErrorMessageApi = (msg: string) => {
        messageApi.error(msg);
    }

    const onSubmitModal = async (incident: IncidentRequest, fileList: UploadFile[]) => {
        try {
            setIsUploading(true);
            if (incident.location === EMPTY_LOCATION) {
                throw new Error("You haven't picked a location of the incident. Please pick one!");
            }

            if (incident.type === 'FLOOD' || !incident.subtype) {
                delete incident.subtype;
            }

            const formData: FormData = new FormData();
            const incidentRequestBlob = new Blob([JSON.stringify(incident)], { type: "application/json" });

            fileList.forEach(file => {
                if (file.originFileObj) {
                    formData.append("images", file.originFileObj);
                }
            });
            formData.set('incidentRequest', incidentRequestBlob);

            const response = await insertFormData(API_INCIDENTS_URL, formData);
            console.log(response);
            messageApi.success("Successfully added an incident! A moderator will look at it shortly...");
        } catch (error) {
            onErrorMessageApi(error.message)
        } finally {
            setIsUploading(false);
            setIsModalOpen(false);
            setManualLocation(null);
            setAutoLocation(null);
        }
    };

    const resolvedLocation = manualLocation ?? autoLocation;

    return (
        <Flex vertical style={{ padding: '12px' }}>
            {contextHolder}
            <Flex justify="space-between" align="center" style={{ marginBottom: 10 }}>
                {/* Filter section */}
                <Flex gap={10} align="center">
                    <FontAwesomeIcon icon={faFilter} style={{ color: 'var(--color-gray)' }} />
                    <IncidentFilters 
                        incidentsData={incidentsData!}
                        onChange={setFilters} 
                    />
                </Flex>

                {/* Auto location section */}
                <Flex justify="flex-end" align="center" gap={10}>
                    <Typography.Title level={5}>Automatic location detection</Typography.Title>
                    <Switch
                        unCheckedChildren="OFF"
                        checkedChildren="ON"
                        checked={switchValue}
                        onChange={handleLocationToggle}
                    />
                </Flex>
            </Flex>
            

            {isLoadingLocation && (
                <Flex justify="center" style={{ marginBottom: 10 }}>
                    <Spin tip="Loading location details..." />
                </Flex>
            )}

            {/* Selectable map */}
            <CustomMap 
                centerCoordinates={getCurrentCenter()} 
                onClick={handleMapClick}
                manualMarkerPosition={manualLocation ? [manualLocation.latitude, manualLocation.longitude] : null}
                dataToDisplay={filteredIncidents.filter(incident => incident.status === IncidentStatus.APPROVED)}
            />
           
            {/* Button for modal call */}
            <Tooltip title="Add an incident">
                <FloatButton 
                    shape="circle"
                    type="primary"
                    style={{ insetInlineEnd: 25 }}
                    icon={<PlusOutlined />} 
                    onClick={() => setIsModalOpen(true)}
                />
            </Tooltip>

            {/* Add modal */}
            <AddIncidentModal 
                isOpen={isModalOpen} 
                onCancel={() => {
                    setIsModalOpen(false);
                    setManualLocation(null);
                }} 
                onSubmit={onSubmitModal} 
                onError={onErrorMessageApi}
                location={resolvedLocation}
                isUploading={isUploading}
            />
        </Flex>
    ); 
}

export default IncidentsPage;
