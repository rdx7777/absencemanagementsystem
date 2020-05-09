import React, {Component} from "react";
import {Container} from 'reactstrap';
import {Link} from "react-router-dom";

class Help4 extends Component {

    render() {
        return (
            <div>
                <Container fluid>
                    <h2 className="mb-3">Absence Management System</h2>
                    <h5>IV. HOW IT WORKS</h5>
                    <ul className="ml-lg-0">
                        <li className="mb-1">When a USER needs to report any absence, she/he logs in to the application
                            and adds their case. The app saves this case in a database and sends an email to the COVER SUPERVISOR
                            with message "Action required".</li>
                        <li className="mb-1">
                            <a>COVER SUPERVISOR logs in to the application, finds the case whose id is given
                            in the email message (usually it is displayed as the first case on the page), edits it when required
                            (especially about ensuring teacher cover) and saves the case (even if nothing was changed – </a>
                            <a className="font-weight-bold text-danger">it is critically required</a>
                            <a>). Then the app sends an email to the appropriate HEAD TEACHER, provided by the USER.</a>
                        </li>
                        <li className="mb-1">HEAD TEACHER logs in to the application, finds the case, edits it
                            ("Absence approved" & "Absence paid" fields must be checked) and saves.
                            The next email is sent to HUMAN RESOURCES SUPERVISOR.</li>
                        <li className="mb-1">HUMAN RESOURCES SUPERVISOR logs in to the application, checks data and saves the case.
                            If "Case resolved" field is marked with "yes", the app sends email to USER with message
                            "Your case has been resolved".</li>
                        <li className="mb-1">If necessary every privileged user can add a case of any employee.</li>
                    </ul>
                    <Link to='/help' className="mr-5">I. How to use this app</Link>
                    <Link to='/help2' className="mr-5">II. Predefined users and cases</Link>
                    <Link to='/help3' className="mr-5">III. Application requirements</Link>
                    <Link to='/help5' className="mr-5">V. TO DO</Link>
                </Container>
            </div>
        )
    }
}

export default Help4;
