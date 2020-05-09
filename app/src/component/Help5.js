import React, {Component} from "react";
import {Container} from 'reactstrap';
import {Link} from "react-router-dom";

class Help5 extends Component {

    render() {
        return (
            <div>
                <Container fluid>
                    <h2 className="mb-3">Absence Management System</h2>
                    <h5>V. TO DO</h5>
                    <ul className="ml-lg-0">
                        <li className="mb-1">Search buttons (User's cases, cases managed by provided Head Teacher, cases by issued dates).</li>
                        <li className="mb-1">Possibility of sorting cases by user's surname & name.</li>
                        <li className="mb-1">Form validation at client side (checking required fields, comparing if User equals Head Teacher).</li>
                        <li className="mb-1">Improve user data editing (displaying password with asterisks, double check new password).</li>
                        <li className="mb-1">Scope of case editing depending on the position (different for Cover Supervisor, Head Teacher and HR Supervisor).</li>
                    </ul>
                    <Link to='/help' className="mr-5">I. How to use this app</Link>
                    <Link to='/help2' className="mr-5">II. Predefined users and cases</Link>
                    <Link to='/help3' className="mr-5">III. Application requirements</Link>
                    <Link to='/help4' className="mr-5">IV. How it works</Link>
                </Container>
            </div>
        )
    }
}

export default Help5;
