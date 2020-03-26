import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavBar from './AppNavBar';
import { Link } from 'react-router-dom';

class CaseList extends Component {

    constructor(props) {
        super(props);
        this.state = {cases: [], isLoading: true};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('api/cases')
            .then(response => response.json())
            .then(data => this.setState({cases: data, isLoading: false}));
    }

    async remove(id) {
        await fetch(`/api/cases/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
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

        const caseList = cases.map(aCase => {
            return <tr key={aCase.id}>
                <td style={{whiteSpace: 'nowrap'}}>{aCase.id}</td>
                <td>{aCase.userId}</td>
                <td>{aCase.headTeacherId}</td>
                <td>{new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'})
                    .format(new Date(aCase.startDate))}</td>
                <td>{new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'})
                    .format(new Date(aCase.endDate))}</td>
                <td>{aCase.isCoverProvided}</td>
                <td>{aCase.isCaseResolved}</td>
                <td>
                    <ButtonGroup>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}} size="sm" color="primary" tag={Link} to={"/cases/" + aCase.id}>Edit Case</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(aCase.id)}>Delete Case</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavBar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/cases">Add Case</Button>
                    </div>
                    <h3>Absence Case List</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="5%">Case id</th>
                            <th width="10%">User id</th>
                            <th width="10%">Head Teacher id</th>
                            <th width="5%">Start date</th>
                            <th width="5%">End date</th>
                            <th>Cover provided?</th>
                            <th>Case resolved?</th>
                            <th width="10%">Actions</th>
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

export default CaseList;
