import React, {Component} from 'react';
import {Button, Container, ListGroup, ListGroupItem, ListGroupItemHeading} from "reactstrap";
import {Link, withRouter} from "react-router-dom";

class CaseDetails extends Component {

    render() {
        const aCase = this.props.location.state.aCase;
        const returnAddress = this.props.location.state.returnAddress;
        var partDayType;
        if (aCase.partDayType === 'AllDay') {
            partDayType = 'All day'
        } else {
            partDayType = aCase.partDayType;
        }
        var isCoverRequired;
        if (aCase.isCoverRequired) {isCoverRequired = 'YES'} else {isCoverRequired = 'NO'}
        var isCoverProvided;
        if (aCase.isCoverProvided) {isCoverProvided = 'YES'} else {isCoverProvided = 'NO'}
        var isAbsenceApproved;
        if (aCase.isApprovedByHeadTeacher) {isAbsenceApproved = 'YES'} else {isAbsenceApproved = 'NO'}
        var isAbsencePaid;
        if (aCase.isAbsencePaid) {isAbsencePaid = 'YES'} else {isAbsencePaid = 'NO'}
        var isCaseResolved;
        if (aCase.isCaseResolved) {isCaseResolved = 'YES'} else {isCaseResolved = 'NO'}

        return (
            <div>
                <Container fluid>
                    <ListGroup>
                        <ListGroupItemHeading>Case details</ListGroupItemHeading>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-2 mb-1">Case id:  {aCase.id}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-4 mb-1">User:  {aCase.user.name} {aCase.user.surname}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-6 mb-1">User title:  {aCase.user.jobTitle}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-5 mb-1">Head Teacher:  {aCase.headTeacher.name} {aCase.headTeacher.surname}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-7 mb-1">Head Teacher title:  {aCase.headTeacher.jobTitle}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-4 mb-1">Start date:  {aCase.startDate}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-4 mb-1">End date:  {aCase.endDate}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-4 mb-1">Part day type:  {partDayType}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-12 mb-1">Absence reason:  {aCase.absenceReason}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-12 mb-1">User comment:  {aCase.userComment}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-2 mb-1">Cover required: {isCoverRequired}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-2 mb-1">Cover provided: {isCoverProvided}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-8 mb-1">Cover Supervisor comment:  {aCase.coverSupervisorComment}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-3 mb-1">Absence approved: {isAbsenceApproved}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-2 mb-1">Absence paid: {isAbsencePaid}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-7 mb-1">Head Teacher comment:  {aCase.headTeacherComment}</ListGroupItem>
                        </div>
                        <div className="row">
                            <ListGroupItem className="rounded block-example border border-dark col-md-10 mb-2">HR Supervisor comment:  {aCase.hrSupervisorComment}</ListGroupItem>
                            <ListGroupItem className="rounded block-example border border-dark col-md-2 mb-2">Case resolved: {isCaseResolved}</ListGroupItem>
                        </div>
                    </ListGroup>
                    <div className="float-right">
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="primary" tag={Link} to={returnAddress}>Back to list</Button>
                    </div>
                </Container>
            </div>
        );
    }
}

export default withRouter(CaseDetails);
