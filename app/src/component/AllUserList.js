import React, {Component} from "react";
import {Button, Container, Table} from "reactstrap";
import {Link} from "react-router-dom";
import authHeader from "../auth/AuthHeader";
import Pagination from "./Pagination";
import apiUrl from "../helper/ApiUrl";

const API_URL = apiUrl();

class AllUserList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            currentUsers: [],
            totalUsers: null,
            currentPage: null,
            requiredPage: null,
            totalPages: null,
            pageLimit: null,
            isLoading: true
        };
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        if (this.props.location.state !== null) {
            this.setState({requiredPage: this.props.location.state.requiredPage});
        }
        this.setState({isLoading: true});

        fetch(API_URL + 'api/users/count', {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({totalUsers: data, isLoading: false}));
    }

    onPageChanged = data => {
        const {currentPage, totalPages, pageLimit} = data;
        const offset = (currentPage - 1) * pageLimit;

        fetch(`${API_URL}api/users?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({currentUsers: data, isLoading: false,
                currentPage: currentPage, totalPages: totalPages, pageLimit: pageLimit}));
    };

    async remove(id) {
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');

        await fetch(`${API_URL}api/users/${id}`, {
            method: 'DELETE',
            headers: headers
        }).then(() => {
            fetch(API_URL + 'api/users/count', {headers: authHeader()})
                .then(response => response.json())
                .then(data => this.setState({totalUsers: data, isLoading: false}))
                .then(() => {
                    const {currentPage, pageLimit} = this.state;
                    const offset = (currentPage - 1) * pageLimit;
                    fetch(`${API_URL}api/users?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
                        .then(response => response.json())
                        .then(data => this.setState({currentUsers: data, isLoading: false}));
                });
        });
    }

    render() {
        const {currentUsers, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const userList = currentUsers.map(user => {
            var isActive;
            isActive = user.isActive ? 'yes' : 'no';
            var role;
            if (user.role === 'ROLE_USER') {role = "user"}
            if (user.role === 'ROLE_CS_SUPERVISOR') {role = "cover supervisor"}
            if (user.role === 'ROLE_HEAD_TEACHER') {role = "head teacher"}
            if (user.role === 'ROLE_HR_SUPERVISOR') {role = "HR supervisor"}
            if (user.role === 'ROLE_ADMIN') {role = "admin"}
            return <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name || ''} {user.surname || ''}</td>
                <td>{user.email}</td>
                <td>{user.jobTitle}</td>
                <td>{isActive}</td>
                <td>{role}</td>
                <td>
                    <Link to={{pathname: "/users/" + user.id, state: {requiredPage: this.state.currentPage}}}>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                size="sm" color="warning">
                            Edit
                        </Button>
                    </Link>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                            size="sm" color="danger"
                            onClick={() => {if (window.confirm('Are you sure you want to delete this user?')) this.remove(user.id)}}>
                        Delete
                    </Button>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Link to={{pathname: "/users/new", state: {requiredPage: this.state.currentPage}}}>
                            <Button color="success">
                                Add User
                            </Button>
                        </Link>
                    </div>
                    <h3>Users</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Id</th>
                            <th width="15%">Name & Surname</th>
                            <th width="20%">Email</th>
                            <th width="20%">Job Title</th>
                            <th width="5%">Active?</th>
                            {/*<th width="5%">User position</th>*/}
                            <th width="10%">Authorisation</th>
                            <th width="15%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {userList}
                        </tbody>
                    </Table>
                    <div className="d-flex flex-row py-4 align-items-center">
                        <Pagination totalRecords={this.state.totalUsers} pageLimit={4} pageNeighbours={1}
                                    requiredPage={this.state.requiredPage}
                                    onPageChanged={this.onPageChanged}
                        />
                    </div>
                </Container>
            </div>
        );
    }
}

export default AllUserList;
