import {HOST_DEVICE} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";

const endpoint = {
    device: '/device'
};

function getDevices(callback) {
    let request = new Request(HOST_DEVICE.device_api + endpoint.device, {
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getClientDevices(params, callback){
    let request = new Request(HOST_DEVICE.device_api + "/person/persondevices/" + params.id, {
        method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}


function getDeviceById(params, callback){
    let request = new Request(HOST_DEVICE.device_api + endpoint.device + "/" + params.id, {
       method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

/*function getPersonByIdPerson(idPerson, callback) {
    let request = new Request(HOST_DEVICE.device_api + endpoint.device + '/username/' + username, {
        method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}*/

function postDevice(user, callback){
    let request = new Request(HOST_DEVICE.device_api + endpoint.device , {
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

function deleteDevice(params, callback) {
    let request = new Request(HOST_DEVICE.device_api + endpoint.device + '/' + params.id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    });

    console.log("URL: " + request.url);
    RestApiClient.performRequest(request, callback);
}

function updateDevice(params, user, callback) {
    let request = new Request(HOST_DEVICE.device_api + endpoint.device + '/' + params.id, {
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
    getDevices,
    getDeviceById,
    //getPersonByUsername,
    postDevice,
    deleteDevice,
    updateDevice,
    getClientDevices
};
