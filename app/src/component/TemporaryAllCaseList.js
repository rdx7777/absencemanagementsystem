import React, { Component } from 'react';
import { Button, Container, Table } from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";
import Pagination from "./Pagination";

class TemporaryAllCaseList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            allCases: [],
            currentCases: [],
            currentPage: null,
            totalPages: null,
            displayButton: "none",
            displayHeadTeacherButton: "none",
            isLoading: true,
        };
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});
        const currentUser = AuthService.getCurrentUser();
        if (currentUser.roles.includes("ROLE_ADMIN")) {
            this.setState({displayButton: ""})
        }
        if (currentUser.roles.includes("ROLE_HEAD_TEACHER")) {
            this.setState({displayHeadTeacherButton: ""})
        }
        fetch('api/cases', {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({allCases: data, isLoading: false}));
    }

    onPageChanged = data => {
        const {allCases} = this.state;
        const {currentPage, totalPages, pageLimit} = data;

        const offset = (currentPage - 1) * pageLimit;
        const currentCases = allCases.slice(offset, offset + pageLimit);

        this.setState({currentPage, currentCases, totalPages});
    };

    async remove(id) {
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');
        await fetch(`/api/cases/${id}`, {
            method: 'DELETE',
            headers: headers
        }).then(() => {
            let updatedCases = [...this.state.allCases].filter(i => i.id !== id);
            this.setState({allCases: updatedCases});
        });
    }

    render() {
        const {allCases, currentPage, currentCases, displayButton, displayHeadTeacherButton, isLoading} = this.state;
        const totalCases = allCases.length;

        if (totalCases === 0) return null;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const caseList = currentCases.map(aCase => {
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
                                state: {aCase: aCase, returnAddress: '/cases'/*, currentPage: currentPage*/}
                            })}>
                        Details</Button>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                            size="sm" color="warning" tag={Link} to={"/cases/" + aCase.id}
                            disabled={aCase.isCaseResolved ? true : false}>
                        Edit</Button>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center', display: `${displayButton}`}}
                            size="sm" color="danger"
                            onClick={() => {if (window.confirm('Are you sure you want to delete this case?'))
                                this.remove(aCase.id);
                                window.location.reload(false);
                            }}>
                        Delete</Button>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center', display: `${displayHeadTeacherButton}`}}
                                color="primary" tag={Link} to="/active_cases_managed_by_headteacher">Show All Active Head Teacher Cases</Button>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="primary" tag={Link} to="/active_cases">Show All Active Cases</Button>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="success" tag={Link} to="/cases/new">Add Employee Case</Button>
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
                        <Pagination totalRecords={totalCases} pageLimit={4} pageNeighbours={1} onPageChanged={this.onPageChanged}/>
                    </div>
                </Container>
            </div>
        );
    }
}

export default withRouter(TemporaryAllCaseList);
