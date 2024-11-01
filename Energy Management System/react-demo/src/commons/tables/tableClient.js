// TABLE CLIENT BACKEND

import React, { Component } from "react";
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import Field from "./fields/Field";
import { Col, Row } from "react-bootstrap";

class TableClient extends Component {
    constructor(props) {
        super(props);

        this.state = {
            data: props.data,
            columns: props.columns,
            search: props.search,
            filters: [],
            pageSize: props.pageSize || 10,
            editingCell: null, // Track the cell being edited
            updateChange: props.updateChange, // Method to call when updating
            selectedRowIndex: false, // Track the selected row index
        };
    }

    search() {}

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

        this.state.filters[index] = {
            value: value.target.value,
            accessor: header
        };

        this.forceUpdate();
    }

    handleDoubleClick = (rowIndex, columnId) => {
        this.setState({ editingCell: { rowIndex, columnId } });
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
            this.props.updateChange(updatedPerson); // Call updateChange with the updated person data
            this.handleBlur();
        }
    }

    handleSelectChange = (event, rowIndex) => {
        const { value } = event.target;
        const updatedData = [...this.state.data];
        updatedData[rowIndex].admin = value === "Admin";
        this.setState({ data: updatedData }, () => {
            const updatedPerson = this.state.data[rowIndex];
            this.props.updateChange(updatedPerson); // Call updateChange right after selection
        });
    }

    handleBlur = () => {
        this.setState({ editingCell: null });
    }

    handleRowClick = (rowIndex) => {
        if (this.state.selectedRowIndex === rowIndex)
            this.setState({ selectedRowIndex: false })
        else
        {
            this.setState({ selectedRowIndex: rowIndex });
            const selectedRowData = this.state.data[rowIndex];
            console.log('Selected Row Data:', selectedRowData);
        }
    }

    renderEditableCell = (cellInfo) => {
        const { editingCell } = this.state;
        const { column, index: rowIndex } = cellInfo;

        if (editingCell && editingCell.rowIndex === rowIndex && editingCell.columnId === column.id) {
            if (column.id === 'admin') {
                // Render a select box for the 'Role' column
                return (
                    <select
                        value={this.state.data[rowIndex].admin ? "Admin" : "Client"}
                        onChange={(e) => this.handleSelectChange(e, rowIndex)}
                        onBlur={this.handleBlur}
                        autoFocus
                    >
                        <option value="Admin">Admin</option>
                        <option value="Client">Client</option>
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
            <div
                onDoubleClick={() => this.handleDoubleClick(rowIndex, column.id)}
            >
                {column.id === 'admin' ? (cellInfo.value ? "Admin" : "Client") : cellInfo.value}
            </div>
        );
    }

    render() {
        let data = this.state.data ? this.state.data.filter(data => this.filter(data)) : [];

        const columns = this.state.columns.map(col => ({
            ...col,
            Cell: this.renderEditableCell // Override the Cell to be editable
        }));

        return (
            <div>
                <Row>
                    {
                        this.state.search.map((header, index) => {
                            return (
                                <Col key={index}>
                                    <div>
                                        <Field id={header.accessor} label={header.accessor}
                                               onChange={(e) => this.handleChange(e, index, header.accessor)} />
                                    </div>
                                </Col>
                            )
                        })
                    }
                </Row>
                <Row>
                    <Col>
                        <ReactTable
                            data={data}
                            resolveData={data => data.map(row => row)}
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
                                    backgroundColor: '#90979e', // Darker color for header background
                                    color: 'white', // White text color for contrast
                                    textAlign: 'center' // Center align header text
                                }
                            })}
                            getTdProps={() => ({
                                style: {
                                    textAlign: 'center' // Center align all cell text
                                }
                            })}
                        />
                    </Col>
                </Row>
            </div>
        )
    }
}

export default TableClient;
