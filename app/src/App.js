import React, {Component} from 'react';
import './App.css';
import Home from './component/Home';
import {BrowserRouter as Router, Link, Route, Switch} from 'react-router-dom';
import CaseEdit from "./component/CaseEdit";
import AllUserList from "./component/AllUserList";
import UserEdit from "./component/UserEdit";
import UserComponent from "./component/UserComponent";
import AddUserCase from "./component/AddUserCase";
import AuthService from "./auth/AuthService";
import SupervisorComponent from "./component/SupervisorComponent";
import ActiveCaseList from "./component/ActiveCaseList";
import ActiveCaseManagedByHeadTeacherList from "./component/ActiveCaseManagedByHeadTeacherList";
import AdminComponent from "./component/AdminComponent";
import CaseDetails from "./component/CaseDetails";
import authHeader from "./auth/AuthHeader";
import AllCaseList from "./component/AllCaseList";
import Help from "./component/Help";
import Help2 from "./component/Help2";
import Help3 from "./component/Help3";
import Help4 from "./component/Help4";
import Help5 from "./component/Help5";
import apiUrl from "./helper/ApiUrl";

const API_URL = apiUrl();

class App extends Component {

    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);
        this.state = {
            showCoverSupervisorBoard: false,
            showHeadTeacherBoard: false,
            showHRSupervisorBoard: false,
            showAdminBoard: false,
            currentUser: undefined,
            userForShow: ''
        };
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();
        if (user) {
            this.setState({
                currentUser: AuthService.getCurrentUser(),
                showCoverSupervisorBoard: user.roles.includes("ROLE_CS_SUPERVISOR"),
                showHeadTeacherBoard: user.roles.includes("ROLE_HEAD_TEACHER"),
                showHRSupervisorBoard: user.roles.includes("ROLE_HR_SUPERVISOR"),
                showAdminBoard: user.roles.includes("ROLE_ADMIN")
            });
            fetch(API_URL + 'api/users/show_user/' + AuthService.getCurrentUser().id, {headers: authHeader()})
                .then(response => response.json())
                .then(data => this.setState({userForShow: data}));
        }
    }

    logOut() {
        AuthService.logout();
    }

    render() {
        const {currentUser, userForShow, showCoverSupervisorBoard, showHeadTeacherBoard, showHRSupervisorBoard, showAdminBoard} = this.state;

        return (
            <Router>
                <div>
                    <nav className="navbar navbar-expand navbar-dark bg-dark">

                        {currentUser && (
                            // <li className="nav-item">
                                <Link to={{pathname: "/user", state: {requiredPage: 1}}} className="navbar-brand">
                                    AMSystem
                                </Link>
                            // </li>
                        )}

                        <Link to={'/help'} className="navbar-brand">
                            [help]
                        </Link>

                        <div className="navbar-nav mr-auto">

                            {currentUser && (
                                <li className="nav-item">
                                    <Link to={{pathname: "/user", state: {requiredPage: 1}}} className="nav-link">
                                        User Board: {userForShow.name} {userForShow.surname} ({userForShow.jobTitle})
                                    </Link>
                                </li>
                            )}

                            {showCoverSupervisorBoard && (
                                <li className="nav-item">
                                    <Link to={{pathname: "/supervisor", state: {requiredPage: 1}}} className="nav-link">
                                        Cover Supervisor Board
                                    </Link>
                                </li>
                            )}

                            {showHeadTeacherBoard && (
                                <li className="nav-item">
                                    <Link to={{pathname: "/supervisor", state: {requiredPage: 1}}} className="nav-link">
                                        Head Teacher Board
                                    </Link>
                                </li>
                            )}

                            {showHRSupervisorBoard && (
                                <li className="nav-item">
                                    <Link to={{pathname: "/supervisor", state: {requiredPage: 1}}} className="nav-link">
                                        HR Supervisor Board
                                    </Link>
                                </li>
                            )}

                            {showAdminBoard && (
                                <div className="navbar-nav ml-auto">
                                    <li className="nav-item">
                                        <Link to={{pathname: "/admin", state: {requiredPage: 1}}} className="nav-link">
                                            Admin Board: Cases
                                        </Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link to={{pathname: "/users", state: {requiredPage: 1}}} className="nav-link">
                                            Admin Board: Users
                                        </Link>
                                    </li>
                                </div>
                            )}
                        </div>

                        {currentUser ? (
                            <div className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <a href="/" className="nav-link" onClick={this.logOut}>
                                        LogOut
                                    </a>
                                </li>
                            </div>
                        ) : (
                            <div className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <Link to={"/"} className="nav-link">
                                        Login
                                    </Link>
                                </li>
                            </div>
                        )}
                    </nav>

                    <div className="container mt-3">
                        <Switch>
                            <Route path='/' exact={true} component={Home}/>
                            <Route path='/user' exact={true} component={UserComponent}/>
                            <Route path='/add_user_case' exact={true} component={AddUserCase}/>
                            <Route path='/supervisor' exact={true} component={SupervisorComponent}/>
                            <Route path='/cases' exact={true} component={AllCaseList}/>
                            <Route path='/active_cases' exact={true} component={ActiveCaseList}/>
                            <Route path='/active_cases_managed_by_headteacher' exact={true}
                                   component={ActiveCaseManagedByHeadTeacherList}/>
                            <Route path='/cases/:id' component={CaseEdit}/>
                            <Route path='/admin' exact={true} component={AdminComponent}/>
                            <Route path='/users' exact={true} component={AllUserList}/>
                            <Route path='/users/:id' component={UserEdit}/>
                            <Route path='/case_details' exact={true} component={CaseDetails}/>
                            <Route path='/help' exact={true} component={Help}/>
                            <Route path='/help2' exact={true} component={Help2}/>
                            <Route path='/help3' exact={true} component={Help3}/>
                            <Route path='/help4' exact={true} component={Help4}/>
                            <Route path='/help5' exact={true} component={Help5}/>
                        </Switch>
                    </div>
                </div>
            </Router>
        )
    }
}

export default App;
