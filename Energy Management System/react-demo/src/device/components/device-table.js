import React from "react";
import Table from "../../commons/tables/table";
import { fetchUsernameById } from "../../person/api/person-api";
import { updateDevice } from "../api/device-api";

const columns = [
    {
        Header: 'Name of device',
        accessor: 'name',
    },
    {
        Header: 'Description',
        accessor: 'description',
    },
    {
        Header: 'Location',
        accessor: 'location',
    },
    {
        Header: 'Consumption',
        accessor: 'max',
    },
    {
        Header: 'Owner',
        accessor: 'owner',
    },
];

const filters = [
    {
        accessor: 'name',
    }
];

class DeviceTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tableData: [],
            loading: true,
        };
    }

    componentDidMount() {
        this.setState({ tableData: this.props.tableData }, this.fetchUsernames);
    }

    fetchUsernames = () => {
        const promises = this.state.tableData.map(device =>
            fetchUsernameById(device.idPerson)
                .then(username => ({ ...device, owner: username }))
                .catch(error => {
                    console.error("Failed to fetch username:", error);
                    return { ...device, owner: 'Unknown' }; // Fallback if fetch fails
                })
        );

        Promise.all(promises).then(tableData => {
            this.setState({ tableData, loading: false });
        });
    }

    handleUpdateDevice = (updatedDevice) => {
        updateDevice({ id: updatedDevice.id }, updatedDevice, (result, status, error) => {
            if (status === 200 || status === 201) {
                console.log("Device updated successfully:", result);
            } else {
                console.error("Failed to update device:", result);
            }
        });
    }

    handleRowClick = (rowIndex) => {
        const selectedDevice = this.state.tableData[rowIndex];
        console.log("DAR AICI", selectedDevice);
        this.props.onRowSelect(selectedDevice);
    }

    render() {
        const { tableData, loading } = this.state;

        if (loading) {
            return <div>Loading...</div>;
        }

        return (
            <Table
                data={tableData}
                columns={columns}
                search={filters}  // Ensure filters are properly referenced here
                pageSize={5}
                updateChange={this.handleUpdateDevice}
                handleRowClick={this.handleRowClick}
            />
        );
    }
}

export default DeviceTable;
