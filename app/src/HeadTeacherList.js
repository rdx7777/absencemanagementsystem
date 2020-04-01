import React, {Component} from 'react';
import {Button, ButtonGroup, Container, Table} from 'reactstrap';
import AppNavBar from './AppNavBar';
import {Link} from 'react-router-dom';

class HeadTeacherList extends Component {

    constructor(props) {
        super(props);
        this.state = {teachers: [], isLoading: true};
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('api/users/headteachers')
            .then(response => response.json())
            .then(data => this.setState({teachers: data, isLoading: false}));
    }

    render() {
        const {teachers, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const headTeacherList = teachers.map(headTeacher => {
            return <tr key={headTeacher.id}>
                <td style={{whiteSpace: 'nowrap'}}>{headTeacher.id}</td>
                <td>{headTeacher.name || ''} {headTeacher.surname || ''}</td>
                <td>
                    <ButtonGroup>
                        <Button style={{whiteSpace: 'nowrap', margin: '0 5px 0 auto', alignSelf: 'center'}} size="sm"
                                color="primary" headTeacher = {this.props}>Select</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavBar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="secondary" tag={Link} to="/cases">Cancel</Button>
                    </div>
                    <h3>Head Teacher List</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="10%">Id</th>
                            <th width="15%">Name</th>
                            <th width="15%">Surname</th>
                        </tr>
                        </thead>
                        <tbody>
                        {headTeacherList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }



}