import { Select, type SelectProps } from "antd";

interface Props {
    options?: SelectProps['options'];
    onChange: (value: string | string[]) => void;
}

function Dropdown({ options, onChange }: Props) {
    return(
        <Select 
            size="large"
            options={options}
            placeholder="Choose status"
            onChange={onChange}
            style={{ width: 200 }} 
        />
    );
}

export default Dropdown;