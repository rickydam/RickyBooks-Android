import React from 'react';
import {Text, View, Image} from 'react-native';

const styles = require('../styles.js');
const homeIcon = require('../images/icons/home.png')

class HomeScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Home',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {homeIcon}
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
