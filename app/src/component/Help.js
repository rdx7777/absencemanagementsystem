import React, {Component} from "react";
import {Container} from 'reactstrap';
import {Link} from "react-router-dom";

class Help extends Component {

    render() {
        return (
            <div>
                <Container fluid>
                    <h2 className="mb-3">Absence Management System</h2>
                    <p className="mb-1">This simple application has been designed to facilitate handling of crew
                        absences in one of public schools in Cumbria.</p>
                    <p className="mb-3">If you want, you can implement it for free in your school (or other organization) –
                        in this case contact me on: <a href="mailto:radek.jerzynski@gmail.com">radek.jerzynski@gmail.com</a></p>
                    <h5>I. HOW TO USE THIS APP</h5>
                    <ul className="ml-lg-0">
                        <li className="mb-0">Logging in is required to use this app. You can log in as: USER,
                            COVER SUPERVISOR, HEAD TEACHER, HUMAN RESOURCES SUPERVISOR or ADMIN.</li>
                        <li className="mb-0">After logging in, each user can see their User Board with all their absence
                            cases, can add their absence case and view the details of the case.
                            User cannot edit or delete absence cases on this dashboard. USER can see only User Board.</li>
                        <li className="mb-0">COVER SUPERVISOR, HEAD TEACHERS, HUMAN RESOURCES SUPERVISOR and ADMIN
                            have also access to Cover Supervisor Board / Head Teacher Board / HR Supervisor Board /
                            Admin Board respectively. These boards display a list of all active cases by default.
                            It is also possible to display a list of all cases using the button "Show All Cases".
                            HEAD TEACHER can also display a list of self-managed active cases.</li>
                        <li className="mb-0">Case details can be viewed by clicking the "Details" button.</li>
                        <li className="mb-0">Each active case (not marked as "resolved") can be edited.</li>
                        <li className="mb-0">Clicking "Add Employee Case" opens a form that allows you to add
                            a new absence case of each user existing in the database.</li>
                        <li className="mb-0">Only ADMIN can delete any case. It happens very rarely.</li>
                        <li className="mb-0">If a case marked as "resolved" must be edited, ADMIN can do this.</li>
                        <li className="mb-0">
                            <a>And only ADMIN can see, add, edit and delete users from the database, including her/himself. </a>
                            <a className="font-weight-bold text-danger">So be very careful</a>
                            <a> – after deleting user with ADMIN authorisation,
                                managing users will be impossible. In this case just contact me (</a>
                            <a href="mailto:radek.jerzynski@gmail.com">radek.jerzynski@gmail.com</a>
                            <a>), I will solve this problem adding a new admin to the database.</a>
                        </li>
                    </ul>
                    <Link to='/help2' className="mr-5">II. Predefined users and cases</Link>
                    <Link to='/help3' className="mr-5">III. Application requirements</Link>
                    <Link to='/help4' className="mr-5">IV. How it works</Link>
                    <Link to='/help5' className="mr-5">V. TO DO</Link>
                </Container>
            </div>
        )
    }
}

export default Help;
