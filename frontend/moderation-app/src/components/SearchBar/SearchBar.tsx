import { Input } from 'antd'

const { Search } = Input;

interface Props {
    placeholder?: string;
    onSearch: () => void;
}

function SearchBar({ placeholder = "Search ...", onSearch }: Props) {
    return(
        <Search 
            placeholder={placeholder}
            allowClear 
            onSearch={onSearch} 
            style={{ width: 200 }} 
            size='large'
        />
    );
}

export default SearchBar;