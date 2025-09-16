import { Flex, Typography } from "antd";
import type { LatLngExpression } from "leaflet";
import CustomMap from "@components/CustomMap/CustomMap";
import type { Location } from "@types/index";

interface Props {
    location: Location;
}

const { Text } = Typography;

function SpecificLocationMap({ location }: Props) {
    const center: LatLngExpression = [location.latitude, location.longitude];

    return (
        <Flex vertical>
            <Flex gap={10} align="center">
                <Text strong>Country: </Text>
                <Text>{location.country}</Text>
            </Flex>
            {/* <Flex gap={10} align="center" style={{ alignItems: 'center' }}>
                <Text strong>State: </Text>
                <Text>{location.state}</Text>
            </Flex> */}
            <Flex gap={10} align="center">
                <Text strong>City: </Text>
                <Text>{location.city}</Text>
            </Flex>
            <Flex gap={10} align="center">
                <Text strong>Address: </Text>
                <Text>{location.address}</Text>
            </Flex>
            
            <CustomMap
                centerCoordinates={center}        
            />
        </Flex> 
    );
}

export default SpecificLocationMap;