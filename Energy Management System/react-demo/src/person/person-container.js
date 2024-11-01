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
import PersonForm from "./components/person-form";
import * as API_USERS from "./api/person-api";
import PersonTable from "./components/person-table";

class PersonContainer extends React.Component {
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
            selectedPerson: false,
        };
    }

    componentDidMount() {
        this.fetchPersons();
    }

    fetchPersons() {
        return API_USERS.getPersons((result, status, err) => {
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
        this.fetchPersons();
    }

    handleRowSelect = (selectedPerson) => {
        // Track the selected person data from the table
        if (this.state.selectedPerson !== false)
            this.setState({ selectedPerson: false })
        else
            this.setState({ selectedPerson });
    }

    deleteSelectedPerson = () => {
        const { selectedPerson } = this.state;
        if (!selectedPerson || selectedPerson===false) {
            alert("Please select a user to delete.");
            return;
        }

        API_USERS.deletePerson({ id: selectedPerson.id }, (result, status, error) => {
            if (status === 200 || status === 201) {
                console.log("Person deleted successfully");
                this.setState({
                    isLoaded: false,
                });
                this.fetchPersons();
            } else {
                console.error("Failed to delete person:", result);
            }
        });
    }

    render() {
        return (
            <div>
                <CardHeader>
                    <strong> User Management </strong>
                </CardHeader>
                <Card>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            <Button color="primary" onClick={this.toggleForm}>Add a user</Button>
                            <Button color="danger" onClick={this.deleteSelectedPerson}>Delete user</Button>
                        </Col>
                    </Row>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            {this.state.isLoaded &&
                                <PersonTable
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
                    <ModalHeader toggle={this.toggleForm}> Add a user: </ModalHeader>
                    <ModalBody>
                        <PersonForm
                            reloadHandler={this.reload}
                        />
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}

export default PersonContainer;
