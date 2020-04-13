import React, {Component} from 'react';
import {ListGroup, ListGroupItem} from "reactstrap";

export default class CaseDetailsOldVer extends Component {

    // ***************** NOT WORKING *******************
    // why?

    // constructor(props) {
    //     super(props);
    // }

    render() {
        const{aCase} = this.props;
        alert(aCase.headTeacher.name + ' ' + aCase.headTeacher.surname);

        var isCoverRequired;
        if (aCase.isCoverRequired) {isCoverRequired = 'yes'} else {isCoverRequired = 'no'}
        var isCoverProvided;
        if (aCase.isCoverProvided) {isCoverProvided = 'yes'} else {isCoverProvided = 'no'}
        var isAbsenceApproved;
        if (aCase.isApprovedByHeadTeacher) {isAbsenceApproved = 'yes'} else {isAbsenceApproved = 'no'}
        var isAbsencePaid;
        if (aCase.isAbsencePaid) {isAbsencePaid = 'yes'} else {isAbsencePaid = 'no'}
        var isCaseResolved;
        if (aCase.isCaseResolved) {isCaseResolved = 'yes'} else {isCaseResolved = 'no'}

        return (
            <React.Fragment>
                <div className="modal fade" id={"showDetailsModal" + aCase.id} tabIndex="-1" role="dialog"
                     aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div className="modal-dialog modal-lg" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="modalTitle">Case Details</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body" id="modalBody">
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
                                    <ListGroupItem>Head Teacher
                                        Comment: {aCase.headTeacherComment}</ListGroupItem>
                                    <ListGroupItem>HR Supervisor
                                        Comment: {aCase.hrSupervisorComment}</ListGroupItem>
                                    <ListGroupItem>Case Resolved: {isCaseResolved}</ListGroupItem>
                                </ListGroup>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <button type="button" className="btn btn-primary" data-toggle="modal"
                        data-target={"#showDetailsModal" + aCase.id}>
                    Details
                </button>
            </React.Fragment>
        );
    }
}
