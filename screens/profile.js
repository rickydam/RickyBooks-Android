import React from 'react';
import {Text, View, Image} from 'react-native';

const styles = require('../styles.js');
const profileIcon = require('../images/icons/profile.png');

class ProfileScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Profile',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {profileIcon}
        style = {[styles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return(
      <View style={styles.container}>
        <Text>This is the profile screen.</Text>
      </View>
    );
  }
}

module.exports = ProfileScreen;
