import React, { Component } from 'react';
import { Button, Container, Table } from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';
import AuthService from "../auth/AuthService";
import authHeader from "../auth/AuthHeader";

class ActiveCaseManagedByHeadTeacherList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            cases: [],
            isLoading: true,
        };
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});
        const currentUser = AuthService.getCurrentUser();
        fetch('api/cases/active/ht/' + currentUser.id, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({cases: data, isLoading: false}));
    }

    async remove(id) {
        const headers = new Headers(authHeader());
        headers.set('Accept', 'application/json');
        headers.set('Content-Type', 'application/json');
        await fetch(`/api/cases/${id}`, {
            method: 'DELETE',
            headers: headers
        }).then(() => {
            let updatedCases = [...this.state.cases].filter(i => i.id !== id);
            this.setState({cases: updatedCases});
        });
    }

    render() {
        const {cases, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const caseList = cases
            .map(aCase => {
                var isProvided;
                if (aCase.isCoverProvided) {
                    isProvided='yes'
                } else {
                    isProvided='no'
                }
                var isResolved;
                if (aCase.isCaseResolved) {
                    isResolved='yes'
                } else {
                    isResolved='no'
                }
                return <tr key={aCase.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{aCase.id}</td>
                    <td>{aCase.user.name || ''} {aCase.user.surname || ''}</td>
                    <td>{aCase.headTeacher.name || ''} {aCase.headTeacher.surname || ''}</td>
                    <td>{new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'})
                        .format(new Date(aCase.startDate))}</td>
                    <td>{new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'})
                        .format(new Date(aCase.endDate))}</td>
                    <td>{isProvided}</td>
                    <td>{isResolved}</td>
                    <td>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                size="sm" color="primary"
                                onClick={() => this.props.history.push({
                                    pathname: '/case_details',
                                    search: '?query=abc',
                                    state: {aCase: aCase, returnAddress: '/active_cases_managed_by_headteacher'}
                                })}>
                            Details</Button>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                size="sm" color="warning" tag={Link} to={"/cases/" + aCase.id}
                                disabled={aCase.isCaseResolved ? true : false}>
                            Edit</Button>
                    </td>
                </tr>
            });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="primary" tag={Link} to="/active_cases">Show All Active Cases</Button>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="primary" tag={Link} to="/cases">Show All Cases</Button>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="success" tag={Link} to="/cases/new">Add Employee Case</Button>
                    </div>
                    <h3>Absence Cases Managed By You (active)</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Id</th>
                            <th width="15%">User</th>
                            <th width="15%">Head Teacher</th>
                            <th width="5%">Start Date</th>
                            <th width="5%">End Date</th>
                            <th width="10%">Cover Provided?</th>
                            <th width="10%">Case Resolved?</th>
                            <th width="20%">Actions</th>
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

export default withRouter(ActiveCaseManagedByHeadTeacherList);
