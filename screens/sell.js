import React from 'react';
import {Text, View, Image} from 'react-native';
const styles = require('../styles.js');

class SellScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Sell',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {require('./icons/sell.png')}
        style = {[styles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={styles.container}>
        <Text>This is the sell screen.</Text>
      </View>
    );
  }
}

module.exports = SellScreen;
