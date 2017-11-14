import React from 'react';
import {Text, View, Image, TouchableOpacity} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const profileStyles = require('../styles/profileStyles.js');
const profileIcon = require('../images/icons/profile.png');
const profileImage = require('../images/ricky.jpg');

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
    return(
      <View style={mainStyles.container}>
        <View style={mainStyles.inline}>
          <View style={mainStyles.titleContainer}>
            <Text style={mainStyles.title}>Profile</Text>
          </View>
          <View style={profileStyles.signOutContainer}>
            <TouchableOpacity style={mainStyles.redButtonSmall}>
              <Text style={mainStyles.buttonText}>SIGN OUT</Text>
            </TouchableOpacity>
          </View>
        </View>
        <Image
          source = {profileImage}
          style = {profileStyles.profileImage}
        />
        <TouchableOpacity style={mainStyles.blueButtonMedium}>
          <Text style={mainStyles.buttonText}>EDIT IMAGE</Text>
        </TouchableOpacity>
        <Text style={profileStyles.name}>Ricky Dam</Text>
        <Text style={profileStyles.normalText}>4th Year Computer Engineering Student</Text>
        <Text style={profileStyles.normalText}>Selling <Text style={mainStyles.boldLarge}>2</Text> textbooks.</Text>
      </View>
    );
  }
}

module.exports = ProfileScreen;
