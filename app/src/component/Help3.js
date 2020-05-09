import React, {Component} from "react";
import {Container} from 'reactstrap';
import {Link} from "react-router-dom";

class Help3 extends Component {

    render() {
        return (
            <div>
                <Container fluid>
                    <h2 className="mb-3">Absence Management System</h2>
                    <h5>III. APPLICATION REQUIREMENTS</h5>
                    <ul className="ml-lg-0">
                        <li className="font-weight-bold mb-1">Users saved in the database</li>
                        <p className="mb-1">The application requires that the following users
                            with properly assigned authorisation roles and positions are saved in the database:</p>
                        <p className="mb-0">1. ADMIN (position: Employee, authorisation: admin)</p>
                        <p className="mb-0">2. COVER SUPERVISOR (position: Cover Supervisor, authorisation: cover supervisor)</p>
                        <p className="mb-0">3. HEAD TEACHER (position: Head Teacher, authorisation: head teacher)</p>
                        <p className="mb-0">4. HUMAN RESOURCES SUPERVISOR (position: HR supervisor, authorisation: HR supervisor)</p>
                        <p className="mb-1">5. and some "simple" USERS of course (position: Employee, authorisation: user)</p>
                        <p className="mb-1"><a>Only one COVER SUPERVISOR and only one HUMAN RESOURCES SUPERVISOR can be saved
                            in the database. </a>
                            <a>When any next of these supervisors are added to database, </a>
                            <a className="text-danger">the app will not work properly, </a>
                            <a>especially when it comes to sending emails to the right person.</a></p>
                        <li className="font-weight-bold mb-0">Adding a new case or editing an existing case
                            (also a new or existing user)</li>
                        <p className="mb-1">When adding a new or editing an existing case all fields in a form marked
                            with asterisks have to be filled.</p>
                        <p className="mb-1">When ADMIN is editing user data, the user's password is displayed in an encrypted version.
                            If you need to change this password, just enter a new password in plain text - it will be encrypted
                            when the app saves it to the database.</p>
                        <p className="mb-0">Because this is really simple application, it does not currently support
                            the following situations (and does not display error messages):</p>
                        <p className="mb-0">
                            <a>– when a Head Teacher is adding or editing her/his own case as a User on any privileged dashboard
                                (it means: not basic User Board) and gives her/himself as a Head Teacher, </a>
                            <a className="text-danger">this case will not be saved or updated in database</a>
                            <a>,</a>
                        </p>
                        <p className="mb-0">
                            <a>– if any of the required field is not filled, </a>
                            <a className="text-danger">a case will not be saved or updated in database</a>
                            <a>.</a>
                        </p>
                    </ul>
                    <Link to='/help' className="mt-0 mr-5">I. How to use this app</Link>
                    <Link to='/help2' className="mt-0 mr-5">II. Predefined users and cases</Link>
                    <Link to='/help4' className="mt-0 mr-5">IV. How it works</Link>
                    <Link to='/help5' className="mt-0 mr-5">V. TO DO</Link>
                </Container>
            </div>
        )
    }
}

export default Help3;
