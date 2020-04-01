import React, {Component} from 'react';

class LoggedUser extends Component {

    constructor(props) {
        super(props);
        this.state = {user: undefined};
    }

    async componentDidMount() {
        await fetch('/')
            .then(response => response.json())
            .then(data => this.setState({user: data}));
    }

    render() {
        return this.state;
    }
}

export default LoggedUser;
