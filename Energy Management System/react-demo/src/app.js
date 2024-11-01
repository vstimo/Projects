import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import NavigationBar from './navigation-bar';
import Home from './home/home';
import PersonContainer from './person/person-container';
import ErrorPage from './commons/errorhandling/error-page';
import styles from './commons/styles/project-style.css';
import Login from "./login/login";
import DeviceContainer from "./device/device-container";
import HomeClient from "./home/homeClient";
import ProtectedRoute from './commons/protectedRoute'; // Import the ProtectedRoute component

class App extends React.Component {
    render() {
        return (
            <div className={styles.back}>
                <Router>
                    <div>
                        <Switch>
                            <Route exact path='/' component={Login} />

                            <ProtectedRoute
                                exact
                                path='/home'
                                component={() => (
                                    <div>
                                        <NavigationBar />
                                        <Home />
                                    </div>
                                )}
                                allowedRoles={['admin']} // Only admin can access /home
                            />

                            <ProtectedRoute
                                exact
                                path='/homeClient'
                                component={() => (
                                    <div>
                                        <HomeClient />
                                    </div>
                                )}
                                allowedRoles={['client']} // Only client can access /homeClient
                            />

                            <ProtectedRoute
                                exact
                                path='/person'
                                component={() => (
                                    <div>
                                        <NavigationBar />
                                        <PersonContainer />
                                    </div>
                                )}
                                allowedRoles={['admin']} // Only admin can access /person
                            />

                            <ProtectedRoute
                                exact
                                path='/device'
                                component={() => (
                                    <div>
                                        <NavigationBar />
                                        <DeviceContainer />
                                    </div>
                                )}
                                allowedRoles={['admin']} // Only admin can access /device
                            />

                            <Route exact path='/error' component={ErrorPage} />

                            <Route component={ErrorPage} />
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }
}

export default App;