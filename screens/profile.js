import React from 'react';
import {Text, View, Image} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const profileIcon = require('../images/icons/profile.png');

class ProfileScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Profile',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {profileIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return(
      <View style={mainStyles.container}>
        <Text>This is the profile screen.</Text>
      </View>
    );
  }
}

module.exports = ProfileScreen;
