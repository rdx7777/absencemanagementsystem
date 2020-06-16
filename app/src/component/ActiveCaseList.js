import React, {Component} from 'react';
import {Button, Container, Table} from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";
import Pagination from "./Pagination";
import apiUrl from "../helper/ApiUrl";

const API_URL = apiUrl();

class ActiveCaseList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            cases: [],
            currentCases: [],
            totalCases: null,
            currentPage: null,
            requiredPage: null,
            totalPages: null,
            pageLimit: null,
            displayButton: "none",
            displayHeadTeacherButton: "none",
            isLoading: true,
        };
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        try {
            if (this.props.location.state !== null) {
                this.setState({requiredPage: this.props.location.state.requiredPage});
            }
            this.setState({isLoading: true});
            const currentUser = AuthService.getCurrentUser();
            if (currentUser.roles.includes("ROLE_ADMIN") || currentUser.roles.includes("ROLE_HR_SUPERVISOR")) {
                this.setState({displayButton: ""})
            }
            if (currentUser.roles.includes("ROLE_HEAD_TEACHER")) {
                this.setState({displayHeadTeacherButton: ""})
            }

            fetch(API_URL + 'api/cases/active/count', {headers: authHeader()})
                .then(response => response.json())
                .then(data => this.setState({totalCases: data, isLoading: false}));
        } catch (e) {
            AuthService.logout();
            this.props.history.push({pathname: '/'});
        }
    }

    onPageChanged = data => {
        const {currentPage, totalPages, pageLimit} = data;
        const offset = (currentPage - 1) * pageLimit;

        fetch(`${API_URL}api/cases/active?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({currentCases: data, isLoading: false,
                currentPage: currentPage, totalPages: totalPages, pageLimit: pageLimit}));
    };

    async remove(id) {
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');
        await fetch(`${API_URL}api/cases/${id}`, {
            method: 'DELETE',
            headers: headers
        }).then(() => {
            fetch(API_URL + 'api/cases/active/count', {headers: authHeader()})
                .then(response => response.json())
                .then(data => this.setState({totalCases: data, isLoading: false}))
                .then(() => {
                    const {currentPage, pageLimit} = this.state;
                    const offset = (currentPage - 1) * pageLimit;
                    fetch(`${API_URL}api/cases/active?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
                        .then(response => response.json())
                        .then(data => this.setState({currentCases: data, isLoading: false}));
                });
        });
    }

    render() {
        const {currentCases, displayButton, displayHeadTeacherButton, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const caseList = currentCases.map(aCase => {
            // var isRequired;
            // if (aCase.isCoverRequired) {isRequired='yes'} else {isRequired='no'}
            var isProvided;
            if (aCase.isCoverProvided === "Awaiting") {isProvided='Await.'} else {isProvided=aCase.isCoverProvided}
            var isApproved;
            if (aCase.isApprovedByHeadTeacher === "Awaiting") {isApproved = 'Await.'} else {isApproved = aCase.isApprovedByHeadTeacher}
            var isPaid;
            if (aCase.isAbsencePaid === "Awaiting") {isPaid = 'Await.'} else {isPaid = aCase.isAbsencePaid}
            // var isResolved;
            // if (aCase.isCaseResolved) {isResolved='yes'} else {isResolved='no'}
            var editButtonDisabled;
            if (aCase.isCaseResolved === "Yes") {editButtonDisabled = true} else {editButtonDisabled = false}
            return <tr key={aCase.id}>
                <td style={{whiteSpace: 'nowrap'}}>{aCase.id}</td>
                <td>{aCase.user.name || ''} {aCase.user.surname || ''}</td>
                <td style={{whiteSpace: 'nowrap'}}>{aCase.startDate}</td>
                <td style={{whiteSpace: 'nowrap'}}>{aCase.endDate}</td>
                <td>{aCase.isCoverRequired}</td>
                <td>{isProvided}</td>
                <td>{isApproved}</td>
                <td>{isPaid}</td>
                <td>{aCase.absenceReason}</td>
                <td>{aCase.isCaseResolved}</td>
                <td>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                            size="sm" color="primary"
                            onClick={() => this.props.history.push({
                                pathname: '/case_details',
                                state: {aCase: aCase, returnAddress: '/active_cases', requiredPage: this.state.currentPage}
                            })}>
                        Details
                    </Button>
                    <Link to={{pathname: "/cases/" + aCase.id,
                        state: {returnAddress: '/active_cases', requiredPage: this.state.currentPage}}}>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                size="sm" color="warning"
                                /*disabled={editButtonDisabled}*/>
                            Edit
                        </Button>
                    </Link>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center', display: `${displayButton}`}}
                            size="sm" color="danger"
                            onClick={() => {if (window.confirm('Are you sure you want to delete this case?'))
                                this.remove(aCase.id)}}>
                        Delete
                    </Button>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Link to={{pathname: '/active_cases_managed_by_headteacher', state: {requiredPage: 1}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center', display: `${displayHeadTeacherButton}`}}
                                    color="primary">
                                Show All Active Head Teacher Cases
                            </Button>
                        </Link>
                        <Link to={{pathname: "/cases", state: {requiredPage: 1}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="primary">
                                Show All Cases
                            </Button>
                        </Link>
                        <Link to={{pathname: '/cases/new',
                            state: {returnAddress: '/active_cases', requiredPage: this.state.currentPage}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="success">
                                Add Employee Case
                            </Button>
                        </Link>
                    </div>
                    <h3>Absence Cases (active)</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Id</th>
                            <th width="10%">User</th>
                            <th width="10%">Start date</th>
                            <th width="10%">End date</th>
                            <th width="3%">Cover req</th>
                            <th width="3%">Cover prvdd</th>
                            <th width="3%">Abs. appd</th>
                            <th width="3%">Abs. paid</th>
                            <th width="15%">Absence reason</th>
                            <th width="3%">Case resolv.</th>
                            <th width="20%"/>
                        </tr>
                        </thead>
                        <tbody>
                        {caseList}
                        </tbody>
                    </Table>
                    <div className="d-flex flex-row py-4 align-items-center">
                        <Pagination totalRecords={this.state.totalCases} pageLimit={4} pageNeighbours={1}
                                    requiredPage={this.state.requiredPage}
                                    onPageChanged={this.onPageChanged}
                        />
                    </div>
                </Container>
            </div>
        );
    }
}

export default withRouter(ActiveCaseList);
