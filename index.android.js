import React, {Component} from 'react';
import {AppRegistry, View} from 'react-native';
import {TabNavigator} from 'react-navigation';

const styles = require('./styles.js');
const HomeScreen = require('./screens/home.js');
const BuyScreen = require('./screens/buy.js');
const SellScreen = require('./screens/sell.js');
const ProfileScreen = require('./screens/profile.js');

const AppNavigator = TabNavigator ({
  TheSellScreen: {screen: SellScreen},
  TheHomeScreen: {screen: HomeScreen},
  TheBuyScreen: {screen: BuyScreen},

  TheProfileScreen: {screen: ProfileScreen},
}, {
  tabBarPosition: 'bottom',
  animationEnabled: true,
  tabBarOptions: {
    indicatorStyle: {
      backgroundColor: 'transparent',
    },
    labelStyle: {
      fontWeight: 'bold',
    },
    style: {
      backgroundColor: 'transparent',
    },
    iconStyle: {
      paddingTop: 30,
    },
    showIcon: true,
    activeTintColor: 'black',
    inactiveTintColor: 'lightgray',
  },
});

export default class RickyBooks extends Component {
  render() {
    return (
      <View style={styles.tabContainer}>
        <AppNavigator/>
      </View>
    );
  }
}

AppRegistry.registerComponent('RickyBooks', () => RickyBooks);
