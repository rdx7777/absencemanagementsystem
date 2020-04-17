import React, {Component} from "react";
import AuthService from "../auth/AuthService";
import {Button, Container, Table} from "reactstrap";
import {Link, withRouter} from "react-router-dom";
import authHeader from "../auth/AuthHeader";

class UserComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            cases: [],
            isLoading: true,
            seen: false
        };
    }

    componentDidMount() {
        this.setState({isLoading: true});
        const currentUser = AuthService.getCurrentUser();
        const id = currentUser.id;
        fetch('api/cases/user/' + id, {headers: authHeader()})
            .then(response => response.json())
            .then(data => this.setState({cases: data, isLoading: false}));
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
                    <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                            size="sm" color="primary"
                            onClick={() => this.props.history.push({
                                pathname: '/case_details',
                                search: '?query=abc',
                                // TODO: remove line above
                                state: {aCase: aCase, returnAddress: '/user'}
                                })}>
                    Details</Button>
                    {/*<CaseDetailsOldVer aCase={aCase}/>*/}
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div className="float-right">
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}}
                                color="success" tag={Link} to="/add_user_case">Add Your Case</Button>
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

export default withRouter(UserComponent);
