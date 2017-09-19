import React from 'react';
import {Text, View, Image} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const homeIcon = require('../images/icons/home.png')

class HomeScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Home',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {homeIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={mainStyles.container}>
        <Text>This is the home screen.</Text>
      </View>
    );
  }
}

module.exports = HomeScreen;
