// session-storage.js
class SessionStorage {
    static setUserInfo(id, username, location, role) {
        sessionStorage.setItem('id', id); // Store the user's id
        sessionStorage.setItem('username', username);
        sessionStorage.setItem('location', location);
        sessionStorage.setItem('role', role);
    }

    static getUserInfo() {
        return {
            id: sessionStorage.getItem('id'), // Retrieve the user's id
            username: sessionStorage.getItem('username'),
            location: sessionStorage.getItem('location'),
            role: sessionStorage.getItem('role'),
        };
    }

    static clearUserInfo() {
        sessionStorage.removeItem('id'); // Clear the user's id
        sessionStorage.removeItem('username');
        sessionStorage.removeItem('location');
        sessionStorage.removeItem('role');
    }
}

export default SessionStorage;
