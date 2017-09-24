import React from 'react';
import {Text, View, Image} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const homeIcon = require('../images/icons/home.png');
const textbooks = require('../images/textbooks.png');

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
    return (
      <View style={mainStyles.container}>
        <Text style={mainStyles.title}>RickyBooks</Text>
        <Image
          source = {textbooks}
          style = {mainStyles.mediumImage}
        />
        <Text>No affiliation with Carleton University.</Text>
      </View>
    );
  }
}

module.exports = HomeScreen;
