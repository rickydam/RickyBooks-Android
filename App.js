import React from 'react';
import {StackNavigator} from 'react-navigation';

const styles = require('./styles.js');
const HomeScreen = require('./screens/home.js');
const TextbookScreen = require('./screens/textbooks.js');

const AppNavigator = StackNavigator ({
  theHomepage: {screen: HomeScreen},
  theTextbooks: {screen: TextbookScreen},
});

export default class App extends React.Component {
  render() {
    return (
      <AppNavigator style={styles.appNavigator} />
    );
  }
}
