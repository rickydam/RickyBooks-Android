import React, {Component} from 'react';
import {AppRegistry, View} from 'react-native';
import {TabNavigator} from 'react-navigation';

const mainStyles = require('./styles/mainStyles.js');
const HomeScreen = require('./screens/home.js');
const BuyScreen = require('./screens/buy.js');
const SellScreen = require('./screens/sell.js');
const ProfileScreen = require('./screens/profile.js');
const MessagesScreen = require('./screens/messages.js');

const AppNavigator = TabNavigator ({
  TheHomeScreen: {screen: HomeScreen},
  TheBuyScreen: {screen: BuyScreen},
  TheMessagesScreen: {screen: MessagesScreen},
  TheSellScreen: {screen: SellScreen},
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
    tabStyle: {
      paddingTop: 10,
      paddingBottom: 10,
      paddingLeft: 0,
      paddingRight: 0,
      marginLeft: -10,
      marginRight: -10,
    },
    showIcon: true,
    activeTintColor: 'black',
    inactiveTintColor: 'lightgray',
  },
});

export default class RickyBooks extends Component {
  render() {
    return (
      <View style={mainStyles.tabContainer}>
        <AppNavigator/>
      </View>
    );
  }
}

AppRegistry.registerComponent('RickyBooks', () => RickyBooks);
