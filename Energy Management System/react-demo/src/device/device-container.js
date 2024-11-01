import React from 'react';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";
import {
    Button,
    Card,
    CardHeader,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Row
} from 'reactstrap';
import DeviceForm from "./components/device-form";
import * as API_DEVICES from "./api/device-api";
import DeviceTable from "./components/device-table";

class DeviceContainer extends React.Component {
    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reload = this.reload.bind(this);

        this.state = {
            collapseForm: false,
            tableData: [],
            isLoaded: false,
            errorStatus: 0,
            error: null,
            selectedDevice: false,
        };
    }

    componentDidMount() {
        this.fetchDevices();
    }

    fetchDevices() {
        return API_DEVICES.getDevices((result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({
                    tableData: result,
                    isLoaded: true,
                });
            } else {
                this.setState({
                    errorStatus: status,
                    error: err,
                });
            }
        });
    }

    toggleForm() {
        this.setState({ selected: !this.state.selected });
    }

    reload() {
        this.setState({
            isLoaded: false,
        });
        this.toggleForm();
        this.fetchDevices();
    }

    handleRowSelect = (selectedDevice) => {
        // Track the selected device data from the table
        if (this.state.selectedDevice !== false)
            this.setState({ selectedDevice: false })
        else
            this.setState({ selectedDevice });
    }

    deleteSelectedDevice = () => {
        const { selectedDevice } = this.state;
        console.log("Device selectat: " , selectedDevice);
        if (!selectedDevice || selectedDevice===false) {
            alert("Please select a device to delete.");
            return;
        }

        API_DEVICES.deleteDevice({ id: selectedDevice.id }, (result, status, error) => {
            if (status === 200 || status === 201) {
                console.log("Device deleted successfully");
                this.setState({
                    isLoaded: false,
                });
                this.fetchDevices();
            } else {
                console.error("Failed to delete device:", result);
            }
        });
    }

    render() {
        return (
            <div>
                <CardHeader>
                    <strong> Device Management </strong>
                </CardHeader>
                <Card>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            <Button color="primary" onClick={this.toggleForm}>Add a device</Button>
                            <Button color="danger" onClick={this.deleteSelectedDevice}>Delete device</Button>
                        </Col>
                    </Row>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            {this.state.isLoaded &&
                                <DeviceTable
                                    tableData={this.state.tableData}
                                    onRowSelect={this.handleRowSelect} // Pass the handleRowSelect method
                                />
                            }
                            {this.state.errorStatus > 0 &&
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            }
                        </Col>
                    </Row>
                </Card>

                <Modal isOpen={this.state.selected} toggle={this.toggleForm}
                       className={this.props.className} size="lg">
                    <ModalHeader toggle={this.toggleForm}> Add a device: </ModalHeader>
                    <ModalBody>
                        <DeviceForm
                            reloadHandler={this.reload}
                        />
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}

export default DeviceContainer;
