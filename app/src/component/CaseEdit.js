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
            headTeachers: [],
            requiredPage: null,
            returnAddress: undefined
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleUserChange = this.handleUserChange.bind(this);
        this.handleHeadTeacherChange = this.handleHeadTeacherChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.location.state !== null) {
            this.setState({requiredPage: this.props.location.state.requiredPage,
                returnAddress: this.props.location.state.returnAddress});
        }
        const headTeachers = await (await fetch('/api/users/headteachers', {headers: authHeader()})).json();
        const users = await (await fetch('/api/users', {headers: authHeader()})).json();
        const currentUser = AuthService.getCurrentUser();
        const filteredUsers = users.filter(u => !(u.id === currentUser.id));
        if (this.props.match.params.id !== 'new') {
            const aCase = await (await fetch(`/api/cases/${this.props.match.params.id}`, {headers: authHeader()})).json();
            this.setState({aCase: aCase, users: filteredUsers, headTeachers: headTeachers});
        } else {
            this.setState({users: filteredUsers, headTeachers: headTeachers})
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
        const userEditingThisCaseId = AuthService.getCurrentUser().id;
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');

        await fetch('/api/cases' + (aCase.id ? '?id=' + aCase.id + '&userId=' + userEditingThisCaseId : ''), {
            method: (aCase.id) ? 'PUT' : 'POST',
            headers: headers,
            body: JSON.stringify(aCase)
        });
        this.props.history.push({
            pathname: `${this.state.returnAddress}`,
            state: {requiredPage: this.state.requiredPage}})
    }

    render() {
        const {aCase, users, headTeachers} = this.state;
        const title = <h2 className="mb-n1">{aCase.id ? 'Edit Case' : 'Add Case'}</h2>;
        const user = users
            .map((user, index) =>
            <option key={index} value={index}>
                {user.name} {user.surname}
            </option>
        );
        const headTeacher = headTeachers
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
                        <FormGroup className="col-md-5 mb-n2">
                            <Label className="mb-0" for="user">Select user (default: </Label>
                            <Label className="mb-0 font-weight-bold" for="user">{aCase.user.name} {aCase.user.surname}</Label>
                            <Label className="mb-0" for="user">) </Label>
                            <Label className="mb-0 text-danger" for="user">*</Label>
                            <Input type="select" name="user" id="user" defaultValue={aCase.user}
                                   onChange={this.handleUserChange}>
                                <option/>
                                {user}
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-5 mb-n2">
                            <Label className="mb-0" for="headTeacher">Select Head Teacher (default: </Label>
                            <Label className="mb-0 font-weight-bold" for="headTeacher">{aCase.headTeacher.name} {aCase.headTeacher.surname}</Label>
                            <Label className="mb-0" for="headTeacher">) </Label>
                            <Label className="mb-0 text-danger" for="headTeacher">*</Label>
                            <Input type="select" name="headTeacher" id="headTeacher" defaultValue={aCase.headTeacher}
                                   onChange={this.handleHeadTeacherChange}>
                                <option/>
                                {headTeacher}
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="startDate">Start date </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="text" name="startDate" id="startDate" value={aCase.startDate}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="endDate">End date </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="text" name="endDate" id="endDate" value={aCase.endDate}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="partDayType">Part day type </Label>
                            <Label className="mb-0 text-danger">*</Label>
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
                        <FormGroup className="col-md-5 mb-n2">
                            <Label className="mb-0" for="absenceReason">Absence reason </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="textarea" name="absenceReason" id="absenceReason" value={aCase.absenceReason}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-5 mb-n2">
                            <Label className="mb-0" for="userComment">User comment</Label>
                            <Input type="textarea" name="userComment" id="userComment" value={aCase.userComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="isCoverRequired">Cover required </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="select" name="isCoverRequired" id="isCoverRequired"
                                   value={aCase.isCoverRequired}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="isCoverProvided">Cover provided </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="select" name="isCoverProvided" id="isCoverProvided"
                                   value={aCase.isCoverProvided}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-6 mb-n2">
                            <Label className="mb-0" for="coverSupervisorComment">Cover Supervisor comment</Label>
                            <Input type="textarea" name="coverSupervisorComment" id="coverSupervisorComment"
                                   value={aCase.coverSupervisorComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="isApprovedByHeadTeacher">Absence approved </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="select" name="isApprovedByHeadTeacher" id="isApprovedByHeadTeacher"
                                   value={aCase.isApprovedByHeadTeacher}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="isAbsencePaid">Absence paid </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="select" name="isAbsencePaid" id="isAbsencePaid"
                                   value={aCase.isAbsencePaid}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-6 mb-n2">
                            <Label className="mb-0" for="headTeacherComment">Head Teacher comment</Label>
                            <Input type="textarea" name="headTeacherComment" id="headTeacherComment"
                                   value={aCase.headTeacherComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-6 mb-n2">
                            <Label className="mb-0" for="hrSupervisorComment">HR Supervisor comment</Label>
                            <Input type="textarea" name="hrSupervisorComment" id="hrSupervisorComment"
                                   value={aCase.hrSupervisorComment}
                                   onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-n2">
                            <Label className="mb-0" for="isCaseResolved">Case resolved </Label>
                            <Label className="mb-0 text-danger">*</Label>
                            <Input type="select" name="isCaseResolved" id="isCaseResolved"
                                   value={aCase.isCaseResolved}
                                   onChange={this.handleChange}>
                                <option/>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <Link className="col-md-1 mb-n2 h-25 align-self-end mr-1"
                              to={{pathname: `${this.state.returnAddress}`, state: {requiredPage: this.state.requiredPage}}}>
                            <Button color="secondary"
                                    // disabled={this.props.match.params.id !== 'new' ? true : false}
                                    style={{display: this.props.match.params.id == 'new' ? "" : "none"}}>
                                Cancel
                            </Button>
                        </Link>
                        <Button className="col-md-1 mb-n2 h-25 align-self-end mr-1"
                                color="primary" type="submit">
                            Save
                        </Button>{' '}
                    </div>
                    <FormGroup>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withRouter(CaseEdit);
