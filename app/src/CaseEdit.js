import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AppNavBar from './AppNavBar';

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
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const headTeachers = await (await fetch('/api/users/headteachers')).json();
        const users = await (await fetch('/api/users')).json();
        if (this.props.match.params.id !== 'new') {
            const aCase = await (await fetch(`/api/cases/${this.props.match.params.id}`)).json();
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

    /*    handleMultiChange(selectedOptions) {
            this.setState({
                aCase: {
                    ...this.state.aCase,
                    headTeacher: selectedOptions
                }
            })
        }*/

    async handleSubmit(event) {
        event.preventDefault();
        const {aCase} = this.state;

        await fetch('/api/cases' + (aCase.id ? '?id=' + aCase.id + '&userId=' + aCase.user.id : ''), {
            method: (aCase.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(aCase)
        });
        this.props.history.push('/cases');
    }

    render() {
        const {aCase, users, headTeachers} = this.state;
        const title = <h2>{aCase.id ? 'Edit Case' : 'Add Case'}</h2>;
        const user = users.map(user =>
            <option value="id" key={user.id}>
                {user.name} {user.surname}
            </option>
        );
        const headTeacher = headTeachers.map(headTeacher =>
            <option value="id" key={headTeacher.id}>
                {headTeacher.name} {headTeacher.surname}
            </option>
        );

        return <div>
            <AppNavBar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            {/*<Label for="userName">User: {aCase.user.name} {aCase.user.surname}</Label>*/}
                            <Label for="user">Select User (default: {aCase.user.name} {aCase.user.surname})</Label>
                            <Input type="select" name="user" id="user" defaultValue={aCase.user}
                                   onChange={this.handleChange}>
                                {user}
                            </Input>
                        </FormGroup>
                    </div>
                    <div className="row">
                        <FormGroup className="col-md-4 mb-3">
                            <Label for="headTeacher">Select Head Teacher
                                (default: {aCase.headTeacher.name} {aCase.headTeacher.surname})</Label>
                            <Input type="select" name="headTeacher" id="headTeacher" value={aCase.headTeacher}
                                   onChange={this.handleChange}>
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
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isCoverProvided">Cover Provided</Label>
                            <Input type="select" name="isCoverProvided" id="isCoverProvided"
                                   value={aCase.isCoverProvided}
                                   onChange={this.handleChange}>
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
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="isAbsencePaid">Absence Paind</Label>
                            <Input type="select" name="isAbsencePaid" id="isAbsencePaid"
                                   value={aCase.isAbsencePaid}
                                   onChange={this.handleChange}>
                                <option value="false">no</option>
                                <option value="true">yes</option>
                            </Input>
                        </FormGroup>
                        <FormGroup className="col-md-6 mb-3">
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
