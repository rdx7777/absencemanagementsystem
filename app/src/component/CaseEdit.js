import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import authHeader from "../auth/AuthHeader";
import AuthService from "../auth/AuthService";

class CaseEdit extends Component {

    emptyCase = {
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
            users: [],
            headTeachers: []
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleUserChange = this.handleUserChange.bind(this);
        this.handleHeadTeacherChange = this.handleHeadTeacherChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const headTeachers = await (await fetch('/api/users/headteachers', {headers: authHeader()})).json();
        const users = await (await fetch('/api/users', {headers: authHeader()})).json();
        if (this.props.match.params.id !== 'new') {
            const aCase = await (await fetch(`/api/cases/${this.props.match.params.id}`, {headers: authHeader()})).json();
            this.setState({aCase: aCase, users: users, headTeachers: headTeachers});
        } else {
            this.setState({users: users, headTeachers: headTeachers})
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

    handleUserChange(event) {
        const {users} = this.state;
        const index = event.target.value;
        let aCase = {...this.state.aCase};
        aCase.user = users[index];
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

        await fetch('/api/cases' + (aCase.id ? '?id=' + aCase.id + '&userId=' + aCase.user.id : ''), {
            method: (aCase.id) ? 'PUT' : 'POST',
            headers: headers,
            body: JSON.stringify(aCase)
        });
        this.props.history.goBack();
    }

    render() {
        const {aCase, users, headTeachers} = this.state;
        const title = <h2>{aCase.id ? 'Edit Case' : 'Add Case'}</h2>;
        const currentUser = AuthService.getCurrentUser();
        const user = users.map((user, index) =>
            <option key={index} value={index}>
                {user.name} {user.surname}
            </option>
        );
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
                            <Label for="user">Select User (default: {aCase.user.name} {aCase.user.surname})</Label>
                            <Input type="select" name="user" id="user" defaultValue={aCase.user}
                                   onChange={this.handleUserChange}>
                                <option/>
                                {user}
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="headTeacher">Select Head Teacher
                                (default: {aCase.headTeacher.name} {aCase.headTeacher.surname})</Label>
                            <Input type="select" name="headTeacher" id="headTeacher" defaultValue={aCase.headTeacher}
                                   onChange={this.handleHeadTeacherChange}>
                                <option/>
                                {headTeacher}
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="startDate">Start Date</Label>
                            <Input type="text" name="startDate" id="startDate" value={aCase.startDate}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="endDate">End Date</Label>
                            <Input type="text" name="endDate" id="endDate" value={aCase.endDate}
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
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isCoverProvided">Cover Provided</Label>
                            <Input type="select" name="isCoverProvided" id="isCoverProvided"
                                   value={aCase.isCoverProvided}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-6 mb-3">
                            <Label for="coverSupervisorComment">Cover Supervisor Comment</Label>
                            <Input type="textarea" name="coverSupervisorComment" id="coverSupervisorComment"
                                   value={aCase.coverSupervisorComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isApprovedByHeadTeacher">Absence Approved</Label>
                            <Input type="select" name="isApprovedByHeadTeacher" id="isApprovedByHeadTeacher"
                                   value={aCase.isApprovedByHeadTeacher}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isAbsencePaid">Absence Paid</Label>
                            <Input type="select" name="isAbsencePaid" id="isAbsencePaid"
                                   value={aCase.isAbsencePaid}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-6 mb-1">
                            <Label for="headTeacherComment">Head Teacher Comment</Label>
                            <Input type="textarea" name="headTeacherComment" id="headTeacherComment"
                                   value={aCase.headTeacherComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-6 mb-3">
                            <Label for="hrSupervisorComment">HR Supervisor Comment</Label>
                            <Input type="textarea" name="hrSupervisorComment" id="hrSupervisorComment"
                                   value={aCase.hrSupervisorComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isCaseResolved">Case Resolved</Label>
                            <Input type="select" name="isCaseResolved" id="isCaseResolved"
                                   value={aCase.isCaseResolved}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                    </div>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/cases">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withRouter(CaseEdit);