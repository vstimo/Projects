// src/auth/AuthContext.js
import React, { createContext, useState, useContext } from 'react';

// Create a context for authentication
const AuthContext = createContext();

// Create a provider component
export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState({ isAuthenticated: false, isAdmin: false });

    const login = (isAdmin) => {
        setAuth({ isAuthenticated: true, isAdmin });
    };

    const logout = () => {
        setAuth({ isAuthenticated: false, isAdmin: false });
    };

    return (
        <AuthContext.Provider value={{ auth, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// Create a custom hook to use the AuthContext
export const useAuth = () => {
    return useContext(AuthContext);
};
