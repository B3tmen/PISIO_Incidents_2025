import { Tabs, type TabsProps } from "antd";
import type { TabItem } from "@types/index";

interface Props {
    defaultActiveKey?: string;
    items: TabItem[];
    onChange?: (activeKey: string) => void;
}

function CustomTabs({ defaultActiveKey = "1", items, onChange }: Props) {
    
    const formattedItems: TabsProps["items"] = items.map((item) => ({
        key: item.key,
        label: (
            <span style={{ display: "flex", alignItems: "center", gap: 4 }}>
                {item.icon}
                {item.label}
            </span>
        ),
        children: item.children        
    }));
    
    return (
        <Tabs
            defaultActiveKey={defaultActiveKey}
            items={formattedItems}
            onChange={onChange}
        >

        </Tabs>
    );
}

export default CustomTabs;