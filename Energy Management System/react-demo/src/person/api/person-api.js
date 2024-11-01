import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";

const endpoint = {
    person: '/person'
};

function getPersons(callback) {
    let request = new Request(HOST.person_api + endpoint.person, {
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonById(params, callback){
    let request = new Request(HOST.person_api + endpoint.person + params.id, {
       method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonByUsername(username, callback) {
    let request = new Request(HOST.person_api + endpoint.person + '/username/' + username, {
        method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

// apel catre client_app
export const fetchUsernameById = async (id) => {
    try {
        const response = await fetch(HOST.person_api + endpoint.person + '/' + id );
        if (response.ok) {
            const person = await response.json();
            return person.username;
        } else {
            console.error("Failed to fetch username for id:", id);
            return null;
        }
    } catch (error) {
        console.error("Error fetching username:", error);
        return null;
    }
};


function postPerson(user, callback){
    let request = new Request(HOST.person_api + endpoint.person , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function deletePerson(params, callback) {
    let request = new Request(HOST.person_api + endpoint.person + '/' + params.id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    });

    console.log("URL: " + request.url);
    RestApiClient.performRequest(request, callback);
}

function updatePerson(params, user, callback) {
    let request = new Request(HOST.person_api + endpoint.person + '/' + params.id, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);
    RestApiClient.performRequest(request, callback);
}

export {
    getPersons,
    getPersonById,
    getPersonByUsername,
    postPerson,
    deletePerson,
    updatePerson
};
