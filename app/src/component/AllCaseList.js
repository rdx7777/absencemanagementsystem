import React, { Component } from 'react';
import { Button, Container, Table } from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";
import Pagination from "./Pagination";
import apiUrl from "../helper/ApiUrl";

const API_URL = apiUrl();

class AllCaseList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            allCases: [],
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
        if (this.props.location.state !== null) {
            this.setState({requiredPage: this.props.location.state.requiredPage});
        }
        this.setState({isLoading: true});
        const currentUser = AuthService.getCurrentUser();
        if (currentUser.roles.includes("ROLE_ADMIN")) {
            this.setState({displayButton: ""})
        }
        if (currentUser.roles.includes("ROLE_HEAD_TEACHER")) {
            this.setState({displayHeadTeacherButton: ""})
        }

        fetch(API_URL + 'api/cases/count', {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({totalCases: data, isLoading: false}));
    }

    onPageChanged = data => {
        const {currentPage, totalPages, pageLimit} = data;
        const offset = (currentPage - 1) * pageLimit;

        fetch(`${API_URL}api/cases?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
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
            fetch(API_URL + 'api/cases/count', {headers: authHeader()})
                .then(response => response.json())
                .then(data => this.setState({totalCases: data, isLoading: false}))
                .then(() => {
                    const {currentPage, pageLimit} = this.state;
                    const offset = (currentPage - 1) * pageLimit;
                    fetch(`${API_URL}api/cases?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
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
            const currentUser = AuthService.getCurrentUser();
            var isRequired;
            if (aCase.isCoverRequired) {isRequired='yes'} else {isRequired='no'}
            var isProvided;
            if (aCase.isCoverProvided) {isProvided='yes'} else {isProvided='no'}
            var isResolved;
            if (aCase.isCaseResolved) {isResolved='yes'} else {isResolved='no'}
            return <tr key={aCase.id}>
                <td style={{whiteSpace: 'nowrap'}}>{aCase.id}</td>
                <td>{aCase.user.name || ''} {aCase.user.surname || ''}</td>
                <td>{aCase.headTeacher.name || ''} {aCase.headTeacher.surname || ''}</td>
                <td>{aCase.startDate}</td>
                <td>{aCase.endDate}</td>
                <td>{isRequired}</td>
                <td>{isProvided}</td>
                <td>{isResolved}</td>
                <td>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                            size="sm" color="primary"
                            onClick={() => this.props.history.push({
                                pathname: '/case_details',
                                state: {aCase: aCase, returnAddress: '/cases', requiredPage: this.state.currentPage}})}>
                        Details
                    </Button>
                    <Link to={{pathname: "/cases/" + aCase.id,
                        state: {returnAddress: '/cases', requiredPage: this.state.currentPage}}}>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                size="sm" color="warning"
                                disabled={aCase.isCaseResolved && !currentUser.roles.includes("ROLE_ADMIN") ? true : false}>
                            Edit
                        </Button>
                    </Link>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center', display: `${displayButton}`}}
                            size="sm" color="danger"
                            onClick={() => {if (window.confirm('Are you sure you want to delete this case?'))
                                this.remove(aCase.id);}}>
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
                        <Link to={{pathname: '/active_cases', state: {requiredPage: 1}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="primary">
                                Show All Active Cases
                            </Button>
                        </Link>
                        <Link to={{pathname: '/cases/new',
                            state: {returnAddress: '/cases', requiredPage: this.state.currentPage}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="success">
                                Add Employee Case
                            </Button>
                        </Link>
                    </div>
                    <h3>Absence Cases (all)</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Id</th>
                            <th width="15%">User</th>
                            <th width="15%">Head Teacher</th>
                            <th width="10%">Start date</th>
                            <th width="10%">End date</th>
                            <th width="5%">Cover required?</th>
                            <th width="5%">Cover provided?</th>
                            <th width="5%">Case resolved?</th>
                            <th width="20%">Actions</th>
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

export default withRouter(AllCaseList);
