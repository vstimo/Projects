// TABEL DEVICE BACKEND

import React, { Component } from "react";
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import Field from "./fields/Field";
import { Col, Row } from "react-bootstrap";
import { getPersons} from "../../device/api/person-api";
import { fetchUsernameById } from "../../person/api/person-api";
import {getDeviceById, updateDevice} from "../../device/api/device-api";

class Table extends Component {
    constructor(props) {
        super(props);

        this.state = {
            data: props.data,
            columns: props.columns,
            search: props.search,
            filters: [],
            pageSize: props.pageSize || 10,
            editingCell: null,
            selectedRowIndex: false,
            persons: [], // Store fetched usernames
        };
    }

    fetchPersons() {
        getPersons((result, status, err) => {
            if (result !== null && status === 200) {
                const personsWithUsernames = [];
                let pendingRequests = result.length;

                result.forEach((person) => {
                    fetchUsernameById(person.idPerson).then((username) => {
                        personsWithUsernames.push({
                            id: person.idPerson,
                            username: username,
                        });
                        pendingRequests--;

                        if (pendingRequests === 0) {
                            this.setState({ persons: personsWithUsernames });
                        }
                    }).catch((error) => {
                        console.error("Error fetching username for id:", person.idPerson, error);
                        pendingRequests--;
                        if (pendingRequests === 0) {
                            this.setState({ persons: personsWithUsernames }); // Update state even if some errors occurred
                        }
                    });
                });
            } else {
                console.error("Failed to fetch persons:", status, err);
                this.setState({
                    errorStatus: status,
                    error: err || "Failed to fetch persons",
                });
            }
        });
    }

    componentDidMount() {
        this.fetchPersons();
    }

    filter(data) {
        let accepted = true;

        this.state.filters.forEach(val => {
            if (String(val.value) === "") {
                accepted = true;
            }

            if (!String(data[val.accessor]).includes(String(val.value)) && !String(val.value).includes(String(data[val.accessor]))) {
                accepted = false;
            }
        });

        return accepted;
    }

    handleChange(value, index, header) {
        if (this.state.filters === undefined)
            this.setState({ filters: [] });

        const newFilters = [...this.state.filters];

        // Update the specific index with the new filter value and accessor
        newFilters[index] = {
            value: value.target.value,
            accessor: header,
        };

        // Set the new filters array in the state
        this.setState({ filters: newFilters });

        this.forceUpdate();
    }

    handleDoubleClick = (rowIndex, columnId) => {
        if (columnId === 'owner') {
            this.setState({ editingCell: { rowIndex, columnId } });
        } else {
            this.setState({ editingCell: { rowIndex, columnId } });
        }
    }

    handleInputChange = (event, rowIndex, columnId) => {
        const { value } = event.target;
        const updatedData = [...this.state.data];
        updatedData[rowIndex][columnId] = value;
        this.setState({ data: updatedData });
    }

    handleInputKeyPress = (event, rowIndex) => {
        if (event.key === 'Enter') {
            const updatedPerson = this.state.data[rowIndex];
            this.props.updateChange(updatedPerson);
            this.handleBlur();
        }
    }

    handleSelectChange = (event, rowIndex) => {
        const { value } = event.target;
        const updatedData = [...this.state.data];
        const selectedRow = updatedData[rowIndex];

        const selectedPerson = this.state.persons.find(person => person.username === value);
        if (selectedPerson) {
            getDeviceById({ id: selectedRow.id }, (device, status, err) => {
                if (device !== null && status === 200) {
                    selectedRow.owner = value;
                    selectedRow.idPerson = selectedPerson.id;

                    const updatedDevice = {
                        ...device,
                        owner: selectedRow.owner,
                        idPerson: selectedRow.idPerson,
                    };

                    updateDevice({ id: selectedRow.id }, updatedDevice, (result, status, err) => {
                        if (status === 200) {
                            this.setState({ data: updatedData });
                            console.log("Device updated successfully");
                        } else {
                            console.error("Failed to update device:", err);
                        }
                    });
                } else {
                    console.error("Failed to fetch device:", err);
                }
            });
        }
    };

    handleBlur = () => {
        this.setState({ editingCell: null });
    }

    handleRowClick = (rowIndex) => {
        if (this.state.selectedRowIndex === rowIndex)
            this.setState({ selectedRowIndex: false })
        else {
            this.setState({ selectedRowIndex: rowIndex });
            const selectedRowData = this.state.data[rowIndex];
            console.log('Selected Row Data:', selectedRowData);
        }
    }

    renderEditableCell = (cellInfo) => {
        const { editingCell, persons } = this.state;
        const { column, index: rowIndex } = cellInfo;

        if (editingCell && editingCell.rowIndex === rowIndex && editingCell.columnId === column.id) {
            if (column.id === 'owner') {
                return (
                    <select
                        value={this.state.data[rowIndex].owner}
                        onChange={(e) => this.handleSelectChange(e, rowIndex)}
                        onBlur={this.handleBlur}
                        autoFocus
                    >
                        {persons.map(person => (
                            <option key={person.id} value={person.username}>{person.username}</option>
                        ))}
                    </select>
                );
            } else {
                return (
                    <input
                        value={this.state.data[rowIndex][column.id]}
                        onChange={(e) => this.handleInputChange(e, rowIndex, column.id)}
                        onKeyPress={(e) => this.handleInputKeyPress(e, rowIndex)}
                        onBlur={this.handleBlur}
                        autoFocus
                    />
                );
            }
        }

        return (
            <div onDoubleClick={() => this.handleDoubleClick(rowIndex, column.id)}>
                {column.id === 'owner' ? cellInfo.value : cellInfo.value}
            </div>
        );
    }

    render() {
        let data = this.state.data ? this.state.data.filter(data => this.filter(data)) : [];
        const columns = this.state.columns.map(col => ({
            ...col,
            Cell: this.renderEditableCell
        }));

        return (
            <div>
                <Row>
                    {this.state.search.map((header, index) => (
                        <Col key={index}>
                            <div>
                                <Field id={header.accessor} label={header.accessor}
                                       onChange={(e) => this.handleChange(e, index, header.accessor)} />
                            </div>
                        </Col>
                    ))}
                </Row>
                <Row>
                    <Col>
                        <ReactTable
                            data={data}
                            columns={columns}
                            defaultPageSize={this.state.pageSize}
                            showPagination={true}
                            style={{
                                height: '300px'
                            }}
                            getTrProps={(state, rowInfo) => {
                                if (!rowInfo) return {};
                                return {
                                    onClick: () => {
                                        this.props.handleRowClick(rowInfo.index);
                                        this.handleRowClick(rowInfo.index);
                                    },
                                    style: {
                                        background: rowInfo.index === this.state.selectedRowIndex ? 'gray' : 'white'
                                    }
                                };
                            }}
                            getTheadProps={() => ({
                                style: {
                                    backgroundColor: '#90979e',
                                    color: 'white',
                                    textAlign: 'center'
                                }
                            })}
                            getTdProps={() => ({
                                style: {
                                    textAlign: 'center'
                                }
                            })}
                        />
                    </Col>
                </Row>
            </div>
        );
    }
}

export default Table;
