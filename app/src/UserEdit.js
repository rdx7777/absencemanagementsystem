import React, {Component} from "react";
import AppNavBar from "./AppNavBar";
import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";
import {Link, withRouter} from "react-router-dom";

class UserEdit extends Component {

    emptyUser = {
        name: '',
        surname: '',
        email: '',
        password: '',
        jobTitle: '',
        isActive: '',
        position: '',
        role: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.emptyUser
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const user = await (await fetch(`/api/users/${this.props.match.params.id}`)).json();
            this.setState({user: user});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let user = {...this.state.user};
        user[name] = value;
        this.setState({user});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {user} = this.state;

        await fetch('/api/users' + (user.id ? '/' + user.id : ''), {
            method: (user.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });
        this.props.history.push('/users');
    }

    render() {
        const {user} = this.state;
        const title = <h2>{user.id ? 'Edit User' : 'Add User'}</h2>;

        return <div>
            <AppNavBar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="name">Name</Label>
                            <Input type="text" name="name" id="name" value={user.name}
                                   onChange={this.handleChange}>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="surname">Surname</Label>
                            <Input type="text" name="surname" id="surname" value={user.surname}
                                   onChange={this.handleChange}>
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="email">Email</Label>
                            <Input type="email" name="email" id="email" value={user.email}
                                   onChange={this.handleChange} placeholder="user email"/>
                        </FormGroup>
                        <FormGroup className="col-md-6 mb-3">
                            <Label for="password">Password</Label>
                            <Input type="text" name="password" id="password" value={user.password}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="jobTitle">Job title</Label>
                            <Input type="text" name="jobTitle" id="jobTitle" value={user.jobTitle}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isActive">User active?</Label>
                            <Input type="select" name="isActive" id="isActive" value={user.isActive}
                                   onChange={this.handleChange}>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="position">Position</Label>
                            <Input type="select" name="position" id="position" value={user.position}
                                   onChange={this.handleChange}>
                                <option value=""></option>
                                <option value="Employee">Employee</option>
                                <option value="CoverSupervisor">Cover Supervisor</option>
                                <option value="HeadTeacher">Head Teacher</option>
                                <option value="HumanResourcesSupervisor">HR Supervisor</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="role">Authorisation</Label>
                            <Input type="select" name="role" id="role" value={user.role}
                                   onChange={this.handleChange}>
                                <option value=""></option>
                                <option value="ROLE_USER">authorisation: user</option>
                                <option value="ROLE_CS_SUPERVISOR">authorisation: cover supervisor</option>
                                <option value="ROLE_HEAD_TEACHER">authorisation: head teacher</option>
                                <option value="ROLE_HR_SUPERVISOR">authorisation: HR supervisor</option>
                                <option value="ROLE_ADMIN">authorisation: admin</option>
                            </Input>
                        </FormGroup>
                    </div>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/users">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withRouter(UserEdit);
