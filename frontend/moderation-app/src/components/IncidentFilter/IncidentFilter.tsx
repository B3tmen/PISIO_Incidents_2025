import { IncidentType } from "@types/enums";
import type { Incident } from "@types/index";
import { Flex, Select } from "antd";
import { useEffect, useMemo, useState } from "react";
import { normalizeCity } from "./filterService";

interface Props {
    incidentsData: Incident[];
    onChange: (filters: {
        type: string;
        city: string;
        time: string;
    }) => void;
}

function IncidentFilters({ incidentsData, onChange }: Props) {
    const [selectedTypeFilter, setSelectedTypeFilter] = useState('All');
    const [selectedCityFilter, setSelectedCityFilter] = useState('All');
    const [selectedTimeFilter, setSelectedTimeFilter] = useState('All');

    const incidentsTypeFilterOptions = [
        { value: 'All', label: 'All types' },
        ...Object.values(IncidentType).map(type => ({ value: type, label: type })),
    ];

    const incidentsLocationFilterOptions = useMemo(() => {
        if (!incidentsData) return [{ value: 'All', label: 'All cities' }];
        const uniqueCities = Array.from(
            new Set(incidentsData.map(i => normalizeCity(i.location.city)).filter(Boolean))
        );
        
        return [
            { value: 'All', label: 'All cities' },
            ...uniqueCities.map(city => ({ value: city, label: city })),
        ];
    }, [incidentsData]);

    const incidentsTimeFilterOptions = [
        { value: 'All', label: 'Show All' },
        { value: '24h', label: 'Last 24h' },
        { value: '7d', label: 'Last 7d' },
        { value: '31d', label: 'Last 31d' },
    ];

    useEffect(() => {
        onChange({
            type: selectedTypeFilter,
            city: selectedCityFilter,
            time: selectedTimeFilter,
        });
    }, [selectedTypeFilter, selectedCityFilter, selectedTimeFilter, onChange]);

  return (
    <Flex gap={10}>
        <Select
            value={selectedTypeFilter}
            onChange={setSelectedTypeFilter}
            options={incidentsTypeFilterOptions}
            style={{ minWidth: 150 }}
        />
        <Select
            value={selectedCityFilter}
            onChange={setSelectedCityFilter}
            options={incidentsLocationFilterOptions}
            style={{ minWidth: 150 }}
        />
        <Select
            value={selectedTimeFilter}
            onChange={setSelectedTimeFilter}
            options={incidentsTimeFilterOptions}
            style={{ minWidth: 150 }}
        />
    </Flex>
  );
}
export default IncidentFilters;