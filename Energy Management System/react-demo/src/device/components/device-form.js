import React from 'react';
import validate from "./validators/device-validators";
import Button from "react-bootstrap/Button";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';
import {getPersons} from "../api/person-api";
import {fetchUsernameById} from "../../person/api/person-api";
import {postDevice} from "../api/device-api";

class DeviceForm extends React.Component {
    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;

        this.state = {
            errorStatus: 0,
            error: null,

            formIsValid: false,
            persons: [],
            selectedOwner: '',

            formControls: {
                name: {
                    value: '',
                    placeholder: 'Enter name of device..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                description: {
                    value: '',
                    placeholder: 'Enter description..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                location: {
                    value: '',
                    placeholder: 'Enter location..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                max: {
                    value: '',
                    placeholder: 'Maximum numbers of hours of consumption..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        ageLimit: { min: 0 },
                        isRequired: true
                    }
                },
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.fetchPersons();
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

    toggleForm() {
        this.setState({ collapseForm: !this.state.collapseForm });
    }

    handleChange = event => {
        const name = event.target.name;
        const value = event.target.value;

        const updatedControls = this.state.formControls;

        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        let formIsValid = true;
        for (let updatedFormElementName in updatedControls) {
            formIsValid = updatedControls[updatedFormElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid
        });
    };

    async handleSubmit() {
        try {
            const selectedPerson = this.state.persons.find(person => person.id === this.state.selectedOwner);

            if (!selectedPerson) {
                throw new Error("Owner not selected or invalid");
            }

            const device = {
                name: this.state.formControls.name.value,
                description: this.state.formControls.description.value,
                location: this.state.formControls.location.value,
                max: parseInt(this.state.formControls.max.value, 10),
                idPerson: selectedPerson.id,
            };

            postDevice(device, (result, status, err) => {
                if (status === 200 || status === 201) {
                    console.log("Successfully inserted device with id: " + result.id);
                    this.reloadHandler();
                } else {
                    this.setState({
                        errorStatus: status,
                        error: "Failed to add device: " + err,
                    });
                }
            });

        } catch (error) {
            console.error("Error submitting device:", error);
            this.setState({
                errorStatus: error.response ? error.response.status : 500,
                error: error.message,
            });
        }
    }

    render() {
        return (
            <div>
                <FormGroup id='name'>
                    <Label for='nameField'> Name: </Label>
                    <Input name='name' id='nameField' placeholder={this.state.formControls.name.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.name.value}
                           touched={this.state.formControls.name.touched ? 1 : 0}
                           valid={this.state.formControls.name.valid}
                           required
                    />
                    {this.state.formControls.name.touched && !this.state.formControls.name.valid &&
                        <div className={"error-message row"}> * Name must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='description'>
                    <Label for='descriptionField'> Description: </Label>
                    <Input name='description' id='descriptionField' placeholder={this.state.formControls.description.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.description.value}
                           touched={this.state.formControls.description.touched ? 1 : 0}
                           valid={this.state.formControls.description.valid}
                           required
                    />
                    {this.state.formControls.description.touched && !this.state.formControls.description.valid &&
                        <div className={"error-message"}> * Description must have at least 3 characters</div>}
                </FormGroup>

                <FormGroup id='location'>
                    <Label for='locationField'> Location: </Label>
                    <Input name='location' id='locationField' placeholder={this.state.formControls.location.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.location.value}
                           touched={this.state.formControls.location.touched ? 1 : 0}
                           valid={this.state.formControls.location.valid}
                           required
                    />
                    {this.state.formControls.location.touched && !this.state.formControls.location.valid &&
                        <div className={"error-message"}> * Location must have at least 3 characters</div>}
                </FormGroup>

                <FormGroup id='max'>
                    <Label for='maxField'> Consumption: </Label>
                    <Input name='max' id='maxField' placeholder={this.state.formControls.max.placeholder}
                           min={0} max={10000} type="number"
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.max.value}
                           touched={this.state.formControls.max.touched ? 1 : 0}
                           valid={this.state.formControls.max.valid}
                           required
                    />
                    {this.state.formControls.max.touched && !this.state.formControls.max.valid &&
                        <div className={"error-message"}> * Maximum range must be above 0</div>}
                </FormGroup>

                <FormGroup>
                    <Label for='ownerSelect'>Choose owner:</Label>
                    <Input
                        type='select'
                        name='ownerSelect'
                        id='ownerSelect'
                        onChange={e => this.setState({ selectedOwner: e.target.value })}
                        value={this.state.selectedOwner}
                    >
                        <option value=''>Select an owner</option>
                        {this.state.persons.map(person => (
                            <option key={person.id} value={person.id}>
                                {person.username}
                            </option>
                        ))}
                    </Input>
                </FormGroup>

                <Row>
                    <Col sm={{ size: '4', offset: 5 }}>
                        <Button type={"submit"} disabled={!this.state.formIsValid} onClick={this.handleSubmit}> Submit </Button>
                    </Col>
                </Row>

                {
                    this.state.errorStatus > 0 &&
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error} />
                }
            </div>
        );
    }
}

export default DeviceForm;
