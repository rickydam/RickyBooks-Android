import React from 'react';
import {Text, View, Image} from 'react-native';
const styles = require('../styles.js');

class BuyScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Buy',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {require('./icons/buy.png')}
        style = {[styles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={styles.container}>
        <Text>This is the buy screen.</Text>
      </View>
    );
  }
}

module.exports = BuyScreen;
