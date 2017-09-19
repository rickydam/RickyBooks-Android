import React from 'react';
import {Text, View, Image} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const sellStyles = require('../styles/sellStyles.js');
const buyIcon = require('../images/icons/buy.png');

class BuyScreen extends React.Component {
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
      <View style={mainStyles.container}>
        <Text>This is the buy screen.</Text>
      </View>
    );
  }
}

module.exports = BuyScreen;
