import axios from "axios";
import apiUrl from "../helper/ApiUrl";

const API_URL = apiUrl();

class AuthService {

    login(username, password) {
        return axios
            .post(API_URL + "api/auth/signin", {
                username,
                password
            })
            .then(response => {
                if (response.data.accessToken) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }
                return response.data;
            });
    }

    logout() {
        localStorage.removeItem("user");
    }

        getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }
}

export default new AuthService();
