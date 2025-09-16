import { Flex, Typography } from "antd";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGlobe, faMapLocation, faPieChart } from "@fortawesome/free-solid-svg-icons";

import CustomTabs from "@components/CustomTabs/CustomTabs";
import AllIncidentsTab from "@components/CustomTabs/AllIncidentsTab/AllIncidentsTab";
import InteractiveMapTab from "@components/CustomTabs/InteractiveMapTab/InteractiveMapTab";
import type { TabItem } from "@types/index";
import './ModerationPage.css'
import AnalyticsTab from "@components/CustomTabs/AnalyticsTab/AnalyticsTab";


function ModerationPage() {
    
    const moderationItems: TabItem[] = [
        {
            key: '1',
            label: 'All incidents',
            icon: <FontAwesomeIcon icon={faGlobe} size="2x" />,
            children: <AllIncidentsTab />
        },
        {
            key: '2',
            label: 'Interactive map',
            icon: <FontAwesomeIcon icon={faMapLocation} size="2x" />,
            children: <InteractiveMapTab />
        },
        {
            key: '3',
            label: 'Analytics',
            icon: <FontAwesomeIcon icon={faPieChart} size="2x" />,
            children: <AnalyticsTab />
        }
    ];

    return(
        <Flex id="root" vertical>
            <Typography>Incident management</Typography>
            <Typography.Title level={2}>Incidents</Typography.Title>

            <CustomTabs
                items={moderationItems}
                // onChange={onChange={(key) => console.log("Active Tab:", key)}}
            />
        </Flex>
    );
}

export default ModerationPage;