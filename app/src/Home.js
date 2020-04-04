import React, { Component } from 'react';
import './App.css';
import AppNavBar from './AppNavBar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {
    render() {
        return (
            <div>
                <AppNavBar/>
                <Container fluid>
                    <Button color="link"><Link to="/cases">Absence Management System</Link></Button>
                    <Button color="link"><Link to="/users">Admin Area</Link></Button>
                </Container>
            </div>
        );
    }
}

export default Home;
