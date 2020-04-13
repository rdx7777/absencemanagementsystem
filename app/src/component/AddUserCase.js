import React, {Component} from 'react';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";
import {Link} from "react-router-dom";

export default class AddUserCase extends Component {

    emptyCase = {
        id: '',
        user: '',
        headTeacher: '',
        startDate: '',
        endDate: '',
        partDayType: '',
        absenceReason: '',
        userComment: '',
        isCoverRequired: '',
        isCoverProvided: '',
        coverSupervisorComment: '',
        isApprovedByHeadTeacher: '',
        isAbsencePaid: '',
        headTeacherComment: '',
        hrSupervisorComment: '',
        isCaseResolved: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            aCase: this.emptyCase,
            user: undefined,
            headTeachers: []
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleHeadTeacherChange = this.handleHeadTeacherChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const currentUser = AuthService.getCurrentUser();
        const user = await (await fetch(`/api/users/${currentUser.id}`, {headers: authHeader()})).json();
        const headTeachers = await (await fetch('/api/users/headteachers', {headers: authHeader()})).json();
        const filteredHeadTeachers = headTeachers.filter(u => !(u.id === currentUser.id));

        const {aCase} = this.state;

        aCase.id = null;
        aCase.user = user;
        aCase.isCoverProvided = false;
        aCase.isApprovedByHeadTeacher = false;
        aCase.isAbsencePaid = false;
        aCase.isCaseResolved = false;

        this.setState({aCase: aCase, user: user, headTeachers: filteredHeadTeachers});
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let aCase = {...this.state.aCase};
        aCase[name] = value;
        this.setState({aCase});
    }

    handleHeadTeacherChange(event) {
        const {headTeachers} = this.state;
        const index = event.target.value;
        let aCase = {...this.state.aCase};
        aCase.headTeacher = headTeachers[index];
        this.setState({aCase});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {aCase} = this.state;
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');

        await fetch('/api/cases', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(aCase)
        });
        this.props.history.goBack();
    }

    render() {
        const {aCase, headTeachers} = this.state;
        const title = <h2>Add Case</h2>;
        const currentUser = AuthService.getCurrentUser();
        const headTeacher = headTeachers
            .filter(ht => !(ht.id === currentUser.id))
            .map((headTeacher, index) =>
            <option key={index} value={index}>
                {headTeacher.name} {headTeacher.surname}
            </option>
        );

        return <div>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label>User: {aCase.user.name} {aCase.user.surname}</Label>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="headTeacher">Select Head Teacher</Label>
                            <Input type="select" name="headTeacher" id="headTeacher" defaultValue={aCase.headTeacher}
                                   onChange={this.handleHeadTeacherChange}>
                                <option/>
                                {headTeacher}
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="startDate">Start Date</Label>
                            <Input type="text" name="startDate" id="startDate" value={aCase.startDate} placeholder="YYYY-MM-DD"
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="endDate">End Date</Label>
                            <Input type="text" name="endDate" id="endDate" value={aCase.endDate} placeholder="YYYY-MM-DD"
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="partDayType">Part Day Type</Label>
                            <Input type="select" name="partDayType" id="partDayType" value={aCase.partDayType}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="Morning">Morning</option>
                                <option value="Afternoon">Afternoon</option>
                                <option value="AllDay">All day</option>
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-5 mb-3">
                            <Label for="absenceReason">Absence Reason</Label>
                            <Input type="textarea" name="absenceReason" id="absenceReason" value={aCase.absenceReason}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-5 mb-3">
                            <Label for="userComment">User Comment</Label>
                            <Input type="textarea" name="userComment" id="userComment" value={aCase.userComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isCoverRequired">Cover Required</Label>
                            <Input type="select" name="isCoverRequired" id="isCoverRequired"
                                   value={aCase.isCoverRequired}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                    </div>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/user">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}
