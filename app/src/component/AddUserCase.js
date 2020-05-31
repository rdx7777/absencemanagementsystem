import React, {Component} from 'react';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";
import {Link} from "react-router-dom";
import apiUrl from "../helper/ApiUrl";

const API_URL = apiUrl();

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
            headTeachers: [],
            requiredPage: null,
            returnAddress: undefined
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleHeadTeacherChange = this.handleHeadTeacherChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        try {
            if (this.props.location.state !== null) {
                this.setState({requiredPage: this.props.location.state.requiredPage,
                    returnAddress: this.props.location.state.returnAddress});
            }
            const currentUser = AuthService.getCurrentUser();
            const user = await (await fetch(`${API_URL}api/users/${currentUser.id}`, {headers: authHeader()})).json();
            const headTeachers = await (await fetch(API_URL + 'api/users/headteachers', {headers: authHeader()})).json();
            const filteredHeadTeachers = headTeachers.filter(u => !(u.id === currentUser.id));

            const {aCase} = this.state;

            aCase.id = null;
            aCase.user = user;
            aCase.isCoverProvided = false;
            aCase.isApprovedByHeadTeacher = false;
            aCase.isAbsencePaid = false;
            aCase.isCaseResolved = false;

            this.setState({aCase: aCase, user: user, headTeachers: filteredHeadTeachers});
        } catch (e) {
            AuthService.logout();
            this.props.history.push({pathname: '/'});
        }
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

        await fetch(API_URL + 'api/cases', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(aCase)
        });
        this.props.history.push({
            pathname: `${this.state.returnAddress}`,
            state: {requiredPage: this.state.requiredPage}})
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
                            <Label>User: <span className="font-weight-bold">{aCase.user.name} {aCase.user.surname}</span></Label>
                        </FormGroup>
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="headTeacher">
                                <span>Select Head Teacher </span>
                                <span className="text-danger">*</span>
                            </Label>
                            <Input type="select" name="headTeacher" id="headTeacher" defaultValue={aCase.headTeacher}
                                   onChange={this.handleHeadTeacherChange}>
                                <option/>
                                {headTeacher}
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="startDate">
                                <span>Start Date </span>
                                <span className="text-danger">*</span>
                            </Label>
                            <Input type="date" name="startDate" id="startDate" value={aCase.startDate} placeholder="YYYY-MM-DD"
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="endDate">
                                <span>End Date </span>
                                <span className="text-danger">*</span>
                            </Label>
                            <Input type="date" name="endDate" id="endDate" value={aCase.endDate} placeholder="YYYY-MM-DD"
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="partDayType">
                                <span>Part Day Type </span>
                                <span className="text-danger">*</span>
                            </Label>
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
                            <Label for="absenceReason">
                                <span>Absence Reason </span>
                                <span className="text-danger">*</span>
                            </Label>
                            <Input type="textarea" name="absenceReason" id="absenceReason" value={aCase.absenceReason}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-5 mb-3">
                            <Label for="userComment">
                                <span>User Comment</span>
                            </Label>
                            <Input type="textarea" name="userComment" id="userComment" value={aCase.userComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isCoverRequired">
                                <span>Cover Required </span>
                                <span className="text-danger">*</span>
                            </Label>
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
                        <Button color="primary" type="submit">
                            Save
                        </Button>{' '}
                        <Link to={{pathname: `${this.state.returnAddress}`, state: {requiredPage: this.state.requiredPage}}}>
                            <Button color="secondary">
                                Cancel
                            </Button>
                        </Link>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}
