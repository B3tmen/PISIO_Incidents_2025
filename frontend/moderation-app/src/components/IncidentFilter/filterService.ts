export const normalizeCity = (city: string) => {
    if (!city) return '';
    const trimmed = city.trim().toLowerCase();
    if (trimmed === 'banjaluka' || trimmed === 'banja luka') 
        return 'Banja Luka';
    
    return city;
};