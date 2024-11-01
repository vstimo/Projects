import React, { useState, useEffect } from 'react';
import { withRouter } from 'react-router-dom';
import "./login.css";
import sample from "../commons/images/login.mp4";
import { getPersonByUsername } from "../person/api/person-api";
import SessionStorage from '../commons/sessionStorage'; // Import the SessionStorage utility

const Login = (props) => {
    const { history } = props;
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [usernameError, setUsernameError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    useEffect(() => {
        // Clear session storage whenever the login page is loaded
        SessionStorage.clearUserInfo();
    }, []);

    const onButtonClick = () => {
        // Reset error messages
        setUsernameError('');
        setPasswordError('');

        getPersonByUsername(username, (result) => {
            if (result !== null) {
                if (result.password === password) {
                    // Login successful
                    console.log('Login successful');

                    // Set session storage
                    const role = result.admin ? 'admin' : 'client';
                    SessionStorage.setUserInfo(result.id, username, history.location.pathname, role);
                    //SessionStorage.setUserInfo(username, history.location.pathname, role);

                    // Redirect based on user role
                    if (result.admin === true) {
                        history.push('/home');
                    } else {
                        history.push('/homeClient');
                    }
                } else {
                    setPasswordError('Incorrect password');
                }
            } else {
                setUsernameError('User not found');
            }
        });
    }

    return (
        <div className={'mainContainer'}>
            <video className='videoTag' autoPlay loop muted>
                <source src={sample} type='video/mp4' />
            </video>

            <div className={'titleContainer'}>
                <div>Login</div>
            </div>
            <br />
            <div className={'inputContainer'}>
                <input
                    value={username}
                    placeholder="Enter your username"
                    onChange={(ev) => setUsername(ev.target.value)}
                    className={'inputBox'}
                />
                <label className="errorLabel">{usernameError}</label>
            </div>
            <br />
            <div className={'inputContainer'}>
                <input
                    value={password}
                    type="password"
                    placeholder="Enter your password"
                    onChange={(ev) => setPassword(ev.target.value)}
                    className={'inputBox'}
                />
                <label className="errorLabel">{passwordError}</label>
            </div>
            <br />
            <div className={'inputContainer'}>
                <input className={'inputButton'} type="button" onClick={onButtonClick} value={'Log in'} />
            </div>
        </div>
    );
}

export default withRouter(Login);
