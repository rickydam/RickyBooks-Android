import React from 'react';
import {Text, View, Image, ScrollView} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');

class BuyDetails extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Buy',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {buyIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <ScrollView contentContainerStyle={mainStyles.container}>
        <Text>This is the buydetails screen.</Text>
      </ScrollView>
    );
  }
}

module.exports = BuyDetails
