import { useState } from "react";
import { Button, Flex, Image, Modal, Popover, Tag } from "antd";
import type { SelectProps } from 'antd';
import { useQuery, useQueryClient } from "@tanstack/react-query";
import type { ColumnsType } from "antd/es/table";

import { getAll } from "@api/services/apiService";
import { faEye } from "@fortawesome/free-solid-svg-icons";
import CustomTable from "@components/CustomTable/CustomTable";
import SearchBar from "@components/SearchBar/SearchBar";
import SpecificLocationMap from "@components/SpecificLocationMap/SpecificLocationMap";
import type { ImageDTO, Incident, IncidentModeration, Location, PageResponse } from "@types/index";
import { API_INCIDENTS_URL, API_MODERATION_PENDING_INCIDENTS_URL } from "@util/constants/apiUrls";
import { moderationService } from "@api/services/moderationService";
import useMessage from "antd/es/message/useMessage";
import { IncidentStatus } from "@types/enums";

import fallbackImage from '@assets/fallbackImage.png';


const incidentsTestData: Incident[] = [
    {
        id: 1,
        type: "1",
        subtype: "1",
        status: "DUPLICATE",
        reportedAt: new Date(),
        description: "1",
        image: "https://fastly.picsum.photos/id/903/200/300.jpg?hmac=bT2dTWTFYT3TyM7cBatAwmhTtJuzlHBXtqn_kH-z3lU",
        location: null,
    },
];


function AllIncidentsTab() {
    const [currentLocation, setCurrentLocation] = useState<Location | null>(null);
    const [locationModalOpen, setLocationModalOpen] = useState(false);
    const [currentPopoverId, setCurrentPopoverId] = useState<number | null>(null); // Track which popover is open
    const [messageApi, contextHolder] = useMessage();
    const queryClient = useQueryClient();

    const { data: incidentsData, isLoading, error } = useQuery({
        queryKey: ['incidents'],
        queryFn: async () => getAll<IncidentModeration>(API_MODERATION_PENDING_INCIDENTS_URL), //API_MODERATION_PENDING_INCIDENTS_URL
    })

    // if (isLoading) return 'Loading...';
    // if (error) return 'Error loading data!';

    console.log(incidentsData);

    const handleOpenChangeActions = (newOpen: boolean, id: number) => {
        setCurrentPopoverId(newOpen ? id : null);
    };

    const dropdownOptions: SelectProps['options'] = [
        {
            label: 'Approve',
            value: 'approve'
        },
        {
            label: 'Deny',
            value: 'deny'
        }
    ];

    const renderPopoverContent = () => {
        return(
            <Flex vertical gap={8}>
                <Button color="green" variant="filled" onClick={() => updateIncidentStatus(currentPopoverId!, IncidentStatus.APPROVED)} >Approve</Button>
                <Button color="red" variant="filled" onClick={() =>  updateIncidentStatus(currentPopoverId!, IncidentStatus.REJECTED)}>Deny</Button>
            </Flex>
        );
    }

    const renderStatusTag = (status: string) => {
        let color = "blue";

        switch(status.toUpperCase()) {
            case "REPORTED":
                color = "blue";  // Neutral/informational
                break;
            case "PENDING":
                color = "orange"; // Warning/attention needed
                break;
            case "APPROVED":
                color = "green";  // Positive/success
                break;
            case "REJECTED":
                color = "red";    // Negative/error
                break;
            case "RESOLVED":
                color = "cyan";   // Completed/technical
                break;
            case "DUPLICATE":
                color = "purple"; // Special case
                break;
            case "CANCELLED":
                color = "gray";   // Inactive/neutral
                break;
            default:
                color = "blue";
        }

        return(
            <Tag color={color}>{status}</Tag>
        );
    }

    const showLocationModal = (location: Location) => {
        setCurrentLocation(location);
        setLocationModalOpen(true);
    }

    const columns: ColumnsType<IncidentModeration> = [
        {
            title: "ID",
            dataIndex: "id",
            key: "id",
        },
        {
            title: "Images",
            dataIndex: "images",
            key: "images",
            render: (images: ImageDTO[]) => (
                <Image.PreviewGroup>
                    {images?.map((img, idx) =>
                        idx === 0 ? (
                            // Visible thumbnail in table
                            <Image
                                key={idx}
                                width={50}
                                height={50}
                                src={img.imageURL}
                                fallback={fallbackImage}
                            />
                        ) : (
                            // Hidden images still included in preview group
                            <Image
                                key={idx}
                                width={50}
                                height={50}
                                src={img.imageURL}
                                fallback={fallbackImage}
                                style={{ display: "none" }}
                            />
                        )
                    )}
                </Image.PreviewGroup>
            )
        },
        {
            title: "Incident ID",
            dataIndex: "incidentId",
            key: "incidentId",
        },
        // {
        //     title: "Type",
        //     dataIndex: "type",
        //     key: "type",
        // },
        // {
        //     title: "Subtype",
        //     dataIndex: "subtype",
        //     key: "subtype",
        // },
        // {
        //     title: "Reported at",
        //     dataIndex: "reportedAt",
        //     key: "reportedAt",
        // },
        // {
        //     title: "Description",
        //     dataIndex: "description",
        //     key: "description",
        // },
        {
            title: "Status",
            dataIndex: "status",
            key: "status",
            render: renderStatusTag
        },
        // {
        //     title: 'Location',
        //     dataIndex: 'location',
        //     key: 'location',
        //     render: (location: Location) => (
        //         <Tooltip title="View location">
        //             <Button shape="circle" icon={<FontAwesomeIcon icon={faEye} />} onClick={() => showLocationModal(location)} />
        //         </Tooltip>
        //     )
        // },
        {
            title: "Actions",
            dataIndex: "action",
            key: "action",
            render: (_, record) => (
                <Popover
                    content={renderPopoverContent()}
                    title="Change status"
                    trigger="click"
                    open={currentPopoverId === record.incidentId}
                    onOpenChange={(newOpen) => handleOpenChangeActions(newOpen, record.incidentId)}
                >
                    <Button variant="text">...</Button>
                </Popover>
            )
        },
    ];

    const onSearch = () => {

    }

    const updateIncidentStatus = async (incidentId: number, status: string) => {
        // Store previous data for rollback
        const previousIncidents = queryClient.getQueryData(['incidents']);

        try {
            // Optimistically update the UI
            queryClient.setQueryData(['incidents'], (old: IncidentModeration[] | undefined) => {
                if (!old) return old;
                return old.map((incident: IncidentModeration) => 
                    incident.incidentId === incidentId 
                        ? { ...incident, status }
                        : incident
                );
            });

            // Make the actual API call
            const response = await moderationService.updateIncidentStatus(incidentId, { status: status });
            if(response.status !== 200) {
                throw new Error("Couldn't update incident. status: " + response.status);
            }

            messageApi.success("Successfully updated incident status!");
            
            // Refetch to ensure data is fresh
            queryClient.invalidateQueries({ queryKey: ['incidents'] });
        } catch(error) {
            // Rollback on error
            queryClient.setQueryData(['incidents'], previousIncidents);
            console.log("ERROR:" + error.message);
            messageApi.error(error.message);
        } finally {
            setCurrentPopoverId(null);
        }
    }


    return (
        <>
            {contextHolder}

            <CustomTable<IncidentModeration>
                columns={columns} 
                dataSource={incidentsData}
                pagination={{ pageSize: 5 }}
            />

            <Modal
                title={`Location: (${currentLocation?.latitude}, ${currentLocation?.longitude})`}
                open={locationModalOpen}
                onOk={() => setLocationModalOpen(false)}
                onCancel={() => setLocationModalOpen(false)}
            >
                <SpecificLocationMap location={currentLocation!} />
            </Modal>
        </>
    );
}

export default AllIncidentsTab;