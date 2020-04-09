import {Button, ListGroup, ListGroupItem} from "reactstrap";
import {Link} from "react-router-dom";
import React from "react";

export default function caseDetails(aCase, returnAddress) {

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
                <ListGroupItem>User: aCase.user.name aCase.user.surname</ListGroupItem>
                <ListGroupItem>Head Teacher: aCase.headTeacher.name aCase.headTeacher.surname</ListGroupItem>
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

// import React, {Component} from 'react';
// import {Link} from 'react-router-dom';
// import {Button} from 'reactstrap';
// import { ListGroup, ListGroupItem } from 'reactstrap';
// import authHeader from "./auth/AuthHeader";
//
// export default class CaseDetails extends Component {
//
//     constructor(props) {
//         super(props);
//         this.state = {
//             aCase: undefined
//         };
//     }
//
//     componentDidMount() {
//         fetch(`/api/cases/${this.props.match.params.id}`, {headers: authHeader()})
//             .then(response => response.json())
//             .then(data => this.setState({aCase: data}));
//     }
//
//     render() {
//         const {aCase} = this.state;
//         var isCoverRequired;
//         if (aCase.isCoverRequired) {isCoverRequired='yes'} else {isCoverRequired='no'}
//         var isCoverProvided;
//         if (aCase.isCoverProvided) {isCoverProvided='yes'} else {isCoverProvided='no'}
//         var isAbsenceApproved;
//         if (aCase.isApprovedByHeadTeacher) {isAbsenceApproved='yes'} else {isAbsenceApproved='no'}
//         var isAbsencePaid;
//         if (aCase.isAbsencePaid) {isAbsencePaid='yes'} else {isAbsencePaid='no'}
//         var isCaseResolved;
//         if (aCase.isCaseResolved) {isCaseResolved='yes'} else {isCaseResolved='no'}
//
//         return (
//             <ListGroup>
//                 <h2>Case details</h2>
//                 <ListGroupItem>Case id: {aCase.id}</ListGroupItem>
//                 <ListGroupItem>User: {aCase.user.name} {aCase.user.surname}</ListGroupItem>
//                 <ListGroupItem>Head Teacher: {aCase.headTeacher.name} {aCase.headTeacher.surname}</ListGroupItem>
//                 <ListGroupItem>Start Date {aCase.startDate}</ListGroupItem>
//                 <ListGroupItem>End Date {aCase.endDate}</ListGroupItem>
//                 <ListGroupItem>Part Day Type: {aCase.partDayType}</ListGroupItem>
//                 <ListGroupItem>Absence Reason: {aCase.absenceReason}</ListGroupItem>
//                 <ListGroupItem>User Comment: {aCase.userComment}</ListGroupItem>
//                 <ListGroupItem>Cover Required: {isCoverRequired}</ListGroupItem>
//                 <ListGroupItem>Cover Provided: {isCoverProvided}</ListGroupItem>
//                 <ListGroupItem>Cover Supervisor Comment: {aCase.coverSupervisorComment}</ListGroupItem>
//                 <ListGroupItem>Absence Approved: {isAbsenceApproved}</ListGroupItem>
//                 <ListGroupItem>Absence Paid: {isAbsencePaid}</ListGroupItem>
//                 <ListGroupItem>Head Teacher Comment: {aCase.headTeacherComment}</ListGroupItem>
//                 <ListGroupItem>HR Supervisor Comment: {aCase.hrSupervisorComment}</ListGroupItem>
//                 <ListGroupItem>Case Resolved: {isCaseResolved}</ListGroupItem>
//                 <Button color="secondary" tag={Link} to="/cases">Back to Case List</Button>
//             </ListGroup>
//         );
//     }
// }
