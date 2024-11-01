import React from "react";
import { updatePerson } from "../api/person-api";
import TableClient from "../../commons/tables/tableClient";

const columns = [
    {
        Header: 'Username',
        accessor: 'username',
    },
    {
        Header: 'Password',
        accessor: 'password',
    },
    {
        Header: 'Age',
        accessor: 'age',
    },
    {
        Header: 'Role',
        accessor: 'admin',
        Cell: ({ value }) =>  (value ? "Admin" : "Client"),
    },
];

const filters = [
    {
        accessor: 'username',
    }
];

class PersonTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tableData: this.props.tableData,
        };
    }

    handleUpdatePerson = (updatedPerson) => {
        updatePerson({ id: updatedPerson.id }, updatedPerson, (result, status, error) => {
            if (status === 200 || status === 201) {
                console.log("Person updated successfully:", result);
            } else {
                console.log("status", status);
                console.log("eroare", error);

                console.error("Failed to update person:", result);
            }
        });
    }

    handleRowClick = (rowIndex) => {
        const selectedPerson = this.state.tableData[rowIndex];
        this.props.onRowSelect(selectedPerson); // Pass the selected person data to PersonContainer
    }

    render() {
        return (
            <TableClient
                data={this.state.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                updateChange={this.handleUpdatePerson}
                handleRowClick={this.handleRowClick} // Pass the row click handler to the TableClient component
            />
        );
    }
}

export default PersonTable;
