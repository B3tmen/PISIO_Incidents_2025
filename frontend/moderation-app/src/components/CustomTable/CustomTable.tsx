import { Table, type TableProps } from "antd";


function CustomTable<T extends object>(props: TableProps<T>) {
  return (
    <Table<T>
      rowKey={(record) => (record as any).id ?? JSON.stringify(record)}
      {...props}
    />
  );
}

export default CustomTable;