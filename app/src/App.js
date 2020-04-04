import React, {Component} from 'react';
import './App.css';
import Home from './Home';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import CaseList from './CaseList';
import CaseEdit from "./CaseEdit";
import UserList from "./UserList";
import UserEdit from "./UserEdit";

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/cases' exact={true} component={CaseList}/>
                    <Route path='/cases/:id' component={CaseEdit}/>
                    <Route path='/users' exact={true} component={UserList}/>
                    <Route path='/users/:id' component={UserEdit}/>
                </Switch>
            </Router>
        )
    }
}

export default App;
