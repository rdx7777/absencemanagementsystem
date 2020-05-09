import React, {Component} from "react";
import {Container} from 'reactstrap';
import {Link} from "react-router-dom";

class Help2 extends Component {

    render() {
        return (
            <div>
                <Container fluid>
                    <h2 className="mb-3">Absence Management System</h2>
                    <h5>II. PREDEFINED USERS AND CASES</h5>
                    <p className="mb-1">To let you test this application there are some predefined users and absence cases in the database.</p>
                    <p className="mb-1">You can log in as one of predefined users:</p>
                    <ul className="ml-lg-0 mb-1">
                        <li className="mb-0">USER (basic) – login: rdx7777.test@gmail.com, rdx7777.test1@gmail.com</li>
                        <li className="mb-0">COVER SUPERVISOR – login: rdx7777.test2@gmail.com</li>
                        <li className="mb-0">HEAD TEACHER – login: rdx7777.test3@gmail.com, rdx7777.test4@gmail.com</li>
                        <li className="mb-0">HUMAN RESOURCES SUPERVISOR – login: rdx7777.test5@gmail.com</li>
                        <li className="mb-0">ADMIN – login: rdx.7777@yahoo.com</li>
                        <li className="mb-0">A password for every account is: test</li>
                    </ul>
                    <p className="mb-1">After logging in as a USER, you can see all user cases, add a case and display details of the case.</p>
                    <p className="mb-1">As a COVER SUPERVISOR, HEAD TEACHER or HUMAN RESOURCES SUPERVISOR you can act as a USER
                        and additionally see all active case in the organization, see all cases (including these marked as "resolved")
                        and display the details of each case. You can also add and edit a case of any user existing in the database.
                        Before doing it check the <Link to='/help3'>requirements</Link>.</p>
                    <p className="mb-1">There are also some predefined absence cases saved in the database – you can explore them after logging in
                        as a specific user.</p>
                    <Link to='/help' className="mr-5">I. How to use this app</Link>
                    <Link to='/help3' className="mr-5">III. Application requirements</Link>
                    <Link to='/help4' className="mr-5">IV. How it works</Link>
                    <Link to='/help5' className="mr-5">V. TO DO</Link>
                </Container>
            </div>
        )
    }
}

export default Help2;
