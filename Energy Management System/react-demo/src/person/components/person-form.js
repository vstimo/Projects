import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/person-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import {Col, Row} from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class PersonForm extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;

        this.state = {
            errorStatus: 0,
            error: null,
            usernameExists: false, // New state to track if username already exists

            formIsValid: false,

            formControls: {
                username: {
                    value: '',
                    placeholder: 'Enter username..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                password: {
                    value: '',
                    placeholder: 'Enter password..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                age: {
                    value: '',
                    placeholder: 'Age..',
                    valid: false,
                    touched: false,
                    validationRules: {
                        ageLimit: { min: 18, max: 120 }, // Updated to an object with min and max
                        isRequired: true
                    }
                },
                isAdmin: {
                    value: false,
                    valid: true,
                    touched: false,
                },
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    toggleForm() {
        this.setState({ collapseForm: !this.state.collapseForm });
    }

    handleChange = event => {
        const name = event.target.name;
        const value = name === 'isAdmin' ? event.target.checked : event.target.value;

        const updatedControls = this.state.formControls;

        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = name === 'isAdmin' || validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        let formIsValid = true;
        for (let updatedFormElementName in updatedControls) {
            formIsValid = updatedControls[updatedFormElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid,
            usernameExists: false // Reset username existence check when form is changed
        });
    };

    checkIfUsernameExists(username, callback) {
        API_USERS.getPersonByUsername(username, (result, status, error) => {
            if (result !== null && status === 200) {
                // Username exists
                this.setState({ usernameExists: true });
                callback(true);
            } else {
                // Username does not exist
                this.setState({ usernameExists: false });
                callback(false);
            }
        });
    }

    registerPerson(person) {
        return API_USERS.postPerson(person, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully inserted person with id: " + result);
                this.reloadHandler();
            } else {
                this.setState({
                    errorStatus: status,
                    error: error
                });
            }
        });
    }

    handleSubmit() {
        const username = this.state.formControls.username.value;

        // Check if username already exists
        this.checkIfUsernameExists(username, (exists) => {
            if (!exists) {

                let person = {
                    username: this.state.formControls.username.value,
                    password: this.state.formControls.password.value,
                    age: parseInt(this.state.formControls.age.value, 10), // Parse age as integer
                    isAdmin: this.state.formControls.isAdmin.value
                };

                console.log(person);
                this.registerPerson(person);
            }
        });
    }

    render() {
        return (
            <div>
                <FormGroup id='username'>
                    <Label for='usernameField'> Name: </Label>
                    <Input name='username' id='usernameField' placeholder={this.state.formControls.username.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.username.value}
                           touched={this.state.formControls.username.touched ? 1 : 0}
                           valid={this.state.formControls.username.valid && !this.state.usernameExists}
                           required
                    />
                    {this.state.formControls.username.touched && !this.state.formControls.username.valid &&
                        <div className={"error-message row"}> * Username must have at least 3 characters </div>}
                    {this.state.usernameExists &&
                        <div className={"error-message row"} style={{ color: 'red' }}>
                            * Username already exists
                        </div>
                    }

                </FormGroup>

                <FormGroup id='password'>
                    <Label for='passwordField'> Password: </Label>
                    <Input name='password' id='passwordField' placeholder={this.state.formControls.password.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.password.value}
                           touched={this.state.formControls.password.touched ? 1 : 0}
                           valid={this.state.formControls.password.valid}
                           required
                    />
                    {this.state.formControls.password.touched && !this.state.formControls.password.valid &&
                        <div className={"error-message"}> * Password must have at least 3 characters</div>}
                </FormGroup>

                <FormGroup id='age'>
                    <Label for='ageField'> Age: </Label>
                    <Input name='age' id='ageField' placeholder={this.state.formControls.age.placeholder}
                           min={0} max={100} type="number"
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.age.value}
                           touched={this.state.formControls.age.touched ? 1 : 0}
                           valid={this.state.formControls.age.valid}
                           required
                    />
                    {this.state.formControls.age.touched && !this.state.formControls.age.valid &&
                        <div className={"error-message"}> * Age must be above 18</div>}
                </FormGroup>

                <FormGroup check>
                    <Label check>
                        <Input type="checkbox" name='isAdmin'
                               onChange={this.handleChange}
                               checked={this.state.formControls.isAdmin.value}
                        />{' '}
                        Want to make the user an admin?
                    </Label>
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

export default PersonForm;
