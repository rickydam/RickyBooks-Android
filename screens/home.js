import React from 'react';
import {Text, View, Image} from 'react-native';
const styles = require('../styles.js');

class HomeScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Home',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {require('./icons/home.png')}
        style = {[styles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={styles.container}>
        <Text>This is the home screen.</Text>
      </View>
    );
  }
}

module.exports = HomeScreen;
