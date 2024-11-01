// ProtectedRoute.js
import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import SessionStorage from './sessionStorage';

const ProtectedRoute = ({ component: Component, allowedRoles, ...rest }) => {
    return (
        <Route
            {...rest}
            render={(props) => {
                const { role } = SessionStorage.getUserInfo();

                if (allowedRoles.includes(role)) {
                    // If the user's role is allowed, render the component
                    return <Component {...props} />;
                } else {
                    return <Redirect to="/error" />;
                }
            }}
        />
    );
};

export default ProtectedRoute;