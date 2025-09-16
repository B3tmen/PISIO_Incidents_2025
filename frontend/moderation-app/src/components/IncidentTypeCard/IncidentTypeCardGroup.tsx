import { Flex } from "antd";
import type { IncidentTypeCardConfig } from "@types/index";
import IncidentTypeCard from "./IncidentTypeCard";

interface Props {
    cards: IncidentTypeCardConfig[];
    selectedId: string | null;
    onSelect: (id: string | null) => void;
}

function IncidentTypeCardGroup({ cards, selectedId, onSelect }: Props) {
    return (
        <Flex align="center" justify="space-between">
            {cards.map((card) => (
                <IncidentTypeCard
                    key={card.id}
                    icon={card.icon}
                    title={card.title}
                    isSelected={selectedId === card.id}
                    onClick={() => 
                        onSelect(selectedId === card.id ? null : card.id)
                    }
                />
            ))}
        </Flex>
    );
}

export default IncidentTypeCardGroup;