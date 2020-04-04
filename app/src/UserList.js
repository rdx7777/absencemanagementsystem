import React, {Component} from "react";
import {Button, ButtonGroup, Container, Table} from "reactstrap";
import {Link} from "react-router-dom";
import AppNavBar from "./AppNavBar";

class UserList extends Component {

    constructor(props) {
        super(props);
        this.state = {users: [], isLoading: true};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('api/users')
            .then(response => response.json())
            .then(data => this.setState({users: data, isLoading: false}));
    }

    async remove(id) {
        await fetch(`/api/users/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedUsers = [...this.state.users].filter(i => i.id !== id);
            this.setState({users: updatedUsers});
        });
    }

    render() {
        const {users, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const userList = users.map(user => {
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
                <td>{user.position}</td>
                <td>{role}</td>
                <td>
                    <ButtonGroup>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}} size="sm"
                                color="primary"
                                tag={Link} to={"/users/" + user.id}>Edit User</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(user.id)}>Delete User</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavBar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/users/new">Add User</Button>
                    </div>
                    <h3>User List</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Id</th>
                            <th width="15%">Name & surname</th>
                            <th width="15%">Email</th>
                            <th width="10%">Job title</th>
                            <th width="5%">Active?</th>
                            <th width="5%">User position</th>
                            <th width="10%">Authorisation</th>
                            <th width="15%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {userList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default UserList;
