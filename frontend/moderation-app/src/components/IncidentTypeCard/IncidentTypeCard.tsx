import type React from "react";
import { Badge, Typography } from "antd";
import './IncidentTypeCard.css'

interface Props {
    icon: React.ReactNode;
    title: string;
    isSelected: boolean;
    onClick: () => void;
}

function IncidentTypeCard({ icon, title, isSelected, onClick }: Props) {

    return(
        <Badge dot={isSelected} status="success" >
            <div className={`container ${isSelected ? 'selected' : ''}`}  onClick={onClick}>
                <div className="icon-container">
                    {icon}
                </div>

                <Typography.Paragraph>{title}</Typography.Paragraph>
            </div>
        </Badge>
        
    );
}

export default IncidentTypeCard;