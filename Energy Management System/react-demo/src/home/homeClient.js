import React from 'react';
import { Container, Jumbotron, Table } from 'reactstrap';
import { getClientDevices } from '../device/api/device-api'; // Adjust the import path as needed
import SessionStorage from '../commons/sessionStorage';

const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    minHeight: "100vh",
    backgroundColor: "#dcd6c5"
};

const textStyle = { color: 'black' };

const tableStyle = {
    borderColor: "black", // Set table border color to brown
};

const cellStyle = {
    borderColor: "black", // Set each cell border color to brown
};

class HomeClient extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            devices: [],
            error: null,
        };
    }

    componentDidMount() {
        const userInfo = SessionStorage.getUserInfo();
        const userId = userInfo.id; // Assuming user ID is stored as 'id'

        if (userId) {
            getClientDevices({ id: userId }, (result, status, err) => {
                if (status === 200 && result) {
                    this.setState({ devices: result });
                } else {
                    this.setState({ error: err });
                    console.error("Failed to fetch devices:", err);
                }
            });
        } else {
            console.error("No user ID found in session storage.");
        }
    }

    render() {
        const { devices, error } = this.state;

        return (
            <div>
                <Jumbotron fluid style={backgroundStyle}>
                    <Container fluid>
                        <h1 className="display-3" style={textStyle}>
                            Integrated Energy Management System for Home-care Assistance
                        </h1>
                        <p className="lead" style={textStyle}>
                            <b>Hi user, from this page you can view the devices you own at your location.</b>
                        </p>
                    </Container>

                    <Container>
                        {error ? (
                            <p style={{ color: 'red' }}>Failed to load devices: {error}</p>
                        ) : (
                            <Table bordered hover style={tableStyle}>
                                <thead>
                                <tr>
                                    <th style={cellStyle}>Name</th>
                                    <th style={cellStyle}>Description</th>
                                    <th style={cellStyle}>Location</th>
                                    <th style={cellStyle}>Max</th>
                                </tr>
                                </thead>
                                <tbody>
                                {devices.map((device) => (
                                    <tr key={device.id}>
                                        <td style={cellStyle}>{device.name}</td>
                                        <td style={cellStyle}>{device.description}</td>
                                        <td style={cellStyle}>{device.location}</td>
                                        <td style={cellStyle}>{device.max}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        )}
                    </Container>
                </Jumbotron>
            </div>
        );
    }
}

export default HomeClient;
