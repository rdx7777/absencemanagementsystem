import React, {Component} from 'react';
import {Button, Container, Table} from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";
import Pagination from "./Pagination";

class ActiveCaseManagedByHeadTeacherList extends Component {

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
        const id = currentUser.id;

        fetch('api/cases/active/count/ht/' + id, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({totalCases: data, isLoading: false}));
    }

    onPageChanged = data => {
        const currentUser = AuthService.getCurrentUser();
        const id = currentUser.id;

        const {currentPage, totalPages, pageLimit} = data;
        const offset = (currentPage - 1) * pageLimit;

        fetch(`api/cases/active/ht/${id}?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({currentCases: data, isLoading: false,
                currentPage: currentPage, totalPages: totalPages, pageLimit: pageLimit}));
    };

    /**
     * unused method for future's purposes
     * **/
    async remove(id) {
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');
        const currentUser = AuthService.getCurrentUser();
        await fetch(`/api/cases/${id}`, {
            method: 'DELETE',
            headers: headers
        }).then(() => {
            fetch('api/cases/active/count/ht/' + currentUser.id, {headers: authHeader()})
                .then(response => response.json())
                .then(data => this.setState({totalCases: data, isLoading: false}))
                .then(() => {
                    const {currentPage, pageLimit} = this.state;
                    const offset = (currentPage - 1) * pageLimit;
                    fetch(`api/cases/active/ht/${id}?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
                        .then(response => response.json())
                        .then(data => this.setState({currentCases: data, isLoading: false}));
                });
        });
    }

    render() {
        const {currentCases, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const caseList = currentCases.map(aCase => {
            var isRequired;
            if (aCase.isCoverRequired) {isRequired='yes'} else {isRequired='no'}
            var isProvided;
            if (aCase.isCoverProvided) {isProvided = 'yes'} else {isProvided = 'no'}
            var isResolved;
            if (aCase.isCaseResolved) {isResolved = 'yes'} else {isResolved = 'no'}
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
                                state: {aCase: aCase, returnAddress: '/active_cases_managed_by_headteacher', requiredPage: this.state.currentPage}
                            })}>
                        Details
                    </Button>
                    <Link to={{pathname: "/cases/" + aCase.id,
                        state: {returnAddress: '/active_cases_managed_by_headteacher', requiredPage: this.state.currentPage}}}>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                size="sm" color="warning"
                                disabled={aCase.isCaseResolved ? true : false}>
                            Edit
                        </Button>
                    </Link>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Link to={{pathname: '/active_cases', state: {requiredPage: 1}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="primary">
                                Show All Active Cases
                            </Button>
                        </Link>
                        <Link to={{pathname: "/cases", state: {requiredPage: 1}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="primary">
                                Show All Cases
                            </Button>
                        </Link>
                        <Link to={{pathname: '/cases/new',
                            state: {returnAddress: '/active_cases_managed_by_headteacher', requiredPage: this.state.currentPage}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="success">
                                Add Employee Case
                            </Button>
                        </Link>
                    </div>
                    <h3>Absence Cases Managed By You (active)</h3>
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

export default withRouter(ActiveCaseManagedByHeadTeacherList);
