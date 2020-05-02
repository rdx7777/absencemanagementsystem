import React, {Component} from "react";
import AuthService from "../auth/AuthService";
import {Button, Container, Table} from "reactstrap";
import {Link, withRouter} from "react-router-dom";
import authHeader from "../auth/AuthHeader";
import Pagination from "./Pagination";

class UserComponent extends Component {

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
            seen: false
        };
    }

    componentDidMount() {
        if (this.props.location.state !== null) {
            // alert("UserComponent: this.props.location.state.requiredPage = "
            //     + this.props.location.state.requiredPage);
            this.setState({requiredPage: this.props.location.state.requiredPage});
        }
        this.setState({isLoading: true});
        const currentUser = AuthService.getCurrentUser();
        const id = currentUser.id;
        // TODO: create methods in controller, service and repository to count all user cases
        fetch('api/cases/user/' + id, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({totalCases: data.length, isLoading: false}));
    }

    onPageChanged = data => {
        const currentUser = AuthService.getCurrentUser();
        const id = currentUser.id;

        const {currentPage, totalPages, pageLimit} = data;
        const offset = (currentPage - 1) * pageLimit;
        // alert("UserComponent: currentPage = " + currentPage + "; totalPages = " + this.state.totalPages);

        fetch(`api/cases/user/${id}?offset=${offset}&limit=${pageLimit}`, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({currentCases: data, isLoading: false,
                currentPage: currentPage, totalPages: totalPages, pageLimit: pageLimit}));
    };

    render() {
        const {currentCases, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const caseList = currentCases.map(aCase => {
            var isResolved;
            if (aCase.isCaseResolved) {
                isResolved = 'yes'
            } else {
                isResolved = 'no'
            }
            return <tr key={aCase.id}>
                <td>{aCase.id}</td>
                <td>{aCase.headTeacher.name || ''} {aCase.headTeacher.surname || ''}</td>
                <td>{aCase.startDate}</td>
                <td>{aCase.endDate}</td>
                <td>{aCase.absenceReason}</td>
                <td>{isResolved}</td>
                <td>
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                            size="sm" color="primary"
                            onClick={() => this.props.history.push({
                                pathname: '/case_details',
                                state: {aCase: aCase, returnAddress: '/user', requiredPage: this.state.currentPage}})}>
                        Details
                    </Button>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Link to={{pathname: '/add_user_case',
                            state: {returnAddress: '/user', requiredPage: this.state.currentPage}}}>
                            <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                    color="success" /*tag={Link} to="/add_user_case"*/>
                                Add Your Case
                            </Button>
                        </Link>
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

export default withRouter(UserComponent);
