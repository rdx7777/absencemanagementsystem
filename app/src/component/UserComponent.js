import React, {Component} from "react";
import AuthService from "../auth/AuthService";
import {Button, ButtonGroup, Container, ListGroup, ListGroupItem, Table} from "reactstrap";
import {Link} from "react-router-dom";
// import caseDetails from "./CaseDetails";
import authHeader from "../auth/AuthHeader";

export default class UserComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            cases: [],
            isLoading: true,
        };
        this.caseDetails = this.caseDetails.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});
        const currentUser = AuthService.getCurrentUser();
        const id = currentUser.id;
        fetch('api/cases/user/' + id, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({cases: data, isLoading: false}));
    }

    caseDetails(aCase, returnAddress) {

        var isCoverRequired;
        if (aCase.isCoverRequired) {isCoverRequired='yes'} else {isCoverRequired='no'}
        var isCoverProvided;
        if (aCase.isCoverProvided) {isCoverProvided='yes'} else {isCoverProvided='no'}
        var isAbsenceApproved;
        if (aCase.isApprovedByHeadTeacher) {isAbsenceApproved='yes'} else {isAbsenceApproved='no'}
        var isAbsencePaid;
        if (aCase.isAbsencePaid) {isAbsencePaid='yes'} else {isAbsencePaid='no'}
        var isCaseResolved;
        if (aCase.isCaseResolved) {isCaseResolved='yes'} else {isCaseResolved='no'}

        return (
            <div>
                <ListGroup>
                    <h2>Case details</h2>
                    <ListGroupItem>Case id: {aCase.id}</ListGroupItem>
                    <ListGroupItem>User: {aCase.user.name} {aCase.user.surname}</ListGroupItem>
                    <ListGroupItem>Head Teacher: {aCase.headTeacher.name} {aCase.headTeacher.surname}</ListGroupItem>
                    <ListGroupItem>Start Date {aCase.startDate}</ListGroupItem>
                    <ListGroupItem>End Date {aCase.endDate}</ListGroupItem>
                    <ListGroupItem>Part Day Type: {aCase.partDayType}</ListGroupItem>
                    <ListGroupItem>Absence Reason: {aCase.absenceReason}</ListGroupItem>
                    <ListGroupItem>User Comment: {aCase.userComment}</ListGroupItem>
                    <ListGroupItem>Cover Required: {isCoverRequired}</ListGroupItem>
                    <ListGroupItem>Cover Provided: {isCoverProvided}</ListGroupItem>
                    <ListGroupItem>Cover Supervisor Comment: {aCase.coverSupervisorComment}</ListGroupItem>
                    <ListGroupItem>Absence Approved: {isAbsenceApproved}</ListGroupItem>
                    <ListGroupItem>Absence Paid: {isAbsencePaid}</ListGroupItem>
                    <ListGroupItem>Head Teacher Comment: {aCase.headTeacherComment}</ListGroupItem>
                    <ListGroupItem>HR Supervisor Comment: {aCase.hrSupervisorComment}</ListGroupItem>
                    <ListGroupItem>Case Resolved: {isCaseResolved}</ListGroupItem>
                    <Button color="secondary" tag={Link} to={returnAddress}>Back to Case List</Button>
                </ListGroup>
            </div>
        );
    }

    render() {
        const {cases, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const caseList = cases.map(aCase => {
            var isResolved;
            if (aCase.isCaseResolved) {
                isResolved = 'yes'
            } else {
                isResolved = 'no'
            }
            return <tr key={aCase.id}>
                <td>{aCase.id}</td>
                <td>{aCase.headTeacher.name || ''} {aCase.headTeacher.surname || ''}</td>
                <td>{new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'})
                    .format(new Date(aCase.startDate))}</td>
                <td>{new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'})
                    .format(new Date(aCase.endDate))}</td>
                <td>{aCase.absenceReason}</td>
                <td>{isResolved}</td>
                <td>
                    <ButtonGroup>
                        {/*<Button size="sm" color="primary" onClick={() => caseDetails(aCase, "/user")}>Details</Button>*/}
                        <Button size="sm" color="primary" onClick={() => {this.caseDetails(aCase, "/user")

                                // var isCoverRequired;
                                // if (aCase.isCoverRequired) {isCoverRequired = 'yes'} else {isCoverRequired = 'no'}
                                // var isCoverProvided;
                                // if (aCase.isCoverProvided) {isCoverProvided = 'yes'} else {isCoverProvided = 'no'}
                                // var isAbsenceApproved;
                                // if (aCase.isApprovedByHeadTeacher) {isAbsenceApproved = 'yes'} else {isAbsenceApproved = 'no'}
                                // var isAbsencePaid;
                                // if (aCase.isAbsencePaid) {isAbsencePaid = 'yes'} else {isAbsencePaid = 'no'}
                                // var isCaseResolved;
                                // if (aCase.isCaseResolved) {isCaseResolved = 'yes'} else {isCaseResolved = 'no'}
                                // return (
                                //     <div>
                                //         <container>
                                //             <ListGroup>
                                //                 <h2>Case details</h2>
                                //                 {/*<ListGroupItem>Case id: {aCase.id}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>User: {aCase.user.name} {aCase.user.surname}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Head Teacher: {aCase.headTeacher.name} {aCase.headTeacher.surname}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Start Date {aCase.startDate}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>End Date {aCase.endDate}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Part Day Type: {aCase.partDayType}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Absence Reason: {aCase.absenceReason}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>User Comment: {aCase.userComment}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Cover Required: {isCoverRequired}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Cover Provided: {isCoverProvided}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Cover Supervisor Comment: {aCase.coverSupervisorComment}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Absence Approved: {isAbsenceApproved}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Absence Paid: {isAbsencePaid}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Head Teacher Comment: {aCase.headTeacherComment}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>HR Supervisor Comment: {aCase.hrSupervisorComment}</ListGroupItem>*/}
                                //                 {/*<ListGroupItem>Case Resolved: {isCaseResolved}</ListGroupItem>*/}
                                //                 <Button color="secondary" tag={Link} to={"/user"}>Back to Case List</Button>
                                //             </ListGroup>
                                //         </container>
                                //     </div>
                                // );
                            }
                        }>Details</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        {/*<ButtonGroup>*/}
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="success" tag={Link} to="/add_user_case">Add Your Case</Button>
                        {/*</ButtonGroup>*/}
                    </div>
                    <h3>Your Absence Cases</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Id</th>
                            <th width="15%">Head Teacher</th>
                            <th width="5%">Start Date</th>
                            <th width="5%">End Date</th>
                            <th width="10%">Absence Reason</th>
                            <th width="10%">Case Resolved?</th>
                            <th width="15%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {caseList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
