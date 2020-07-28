import * as React from 'react';
import { hot } from 'react-hot-loader';
import World from './World';

class HelloWorld extends React.Component {
  render() {
    return <h1>Hello <World /></h1>;
  }
}

export default hot(module)(HelloWorld);