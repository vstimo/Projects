import React from 'react';
import {Container, Jumbotron} from 'reactstrap';

const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    height: "1000px",
    backgroundColor: "#dcd6c5"
};
const textStyle = {color: 'black', };

class Home extends React.Component {
    render() {
        return (
            <div>
                <Jumbotron fluid style={backgroundStyle}>
                    <Container fluid>
                        <h1 className="display-3" style={textStyle}>Integrated Energy Management System for Home-care assistance</h1>
                        <p className="lead" style={textStyle}> <b>Hi admin, from this page you can access the data of clients and devices,
                            check the menu above to go to the desired page.</b> </p>
                        <hr className="my-2"/>
                        <p  style={textStyle}> <b>This assignment represents the first module of the distributed software system "Integrated Energy
                            Management System for Home-care assistance that represents the final project for the Distributed Systems course. </b> </p>
                    </Container>
                </Jumbotron>

            </div>
        )
    };
}

export default Home
