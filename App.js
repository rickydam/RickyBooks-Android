import React, {Component} from 'react';
import {AppRegistry, View} from 'react-native';
import {TabNavigator, StackNavigator} from 'react-navigation';

const mainStyles = require('./styles/mainStyles.js');
const HomeScreen = require('./screens/home.js');
const BuyScreen = require('./screens/buy.js');
const BuyDetailsScreen = require('./screens/buyDetails.js');
const MessagesScreen = require('./screens/messages.js');
const MessagesDetailsScreen = require('./screens/messagesDetails.js');
const SellScreen = require('./screens/sell.js');
const ProfileScreen = require('./screens/profile.js');

const TheBuyStackNavigator = StackNavigator ({
  TheBuyScreen: {screen: BuyScreen},
  TheBuyDetailsScreen: {screen: BuyDetailsScreen},
}, {
  headerMode: 'none',
});

const TheMessagesStackNavigator = StackNavigator ({
  TheMessagesScreen: {screen: MessagesScreen},
  TheMessagesDetailsScreen: {screen: MessagesDetailsScreen},
}, {
  headerMode: 'none',
});

const TheTabNavigator = TabNavigator ({
  TheHomeScreen: {screen: HomeScreen},
  Buy: {screen: TheBuyStackNavigator},
  Messages: {screen: TheMessagesStackNavigator},
  TheSellScreen: {screen: SellScreen},
  TheProfileScreen: {screen: ProfileScreen},
}, {
  tabBarPosition: 'bottom',
  animationEnabled: false,
  tabBarOptions: {
    indicatorStyle: {
      backgroundColor: 'transparent',
    },
    labelStyle: {
      fontWeight: 'bold',
      width: 80,
    },
    style: {
      backgroundColor: 'transparent',
    },
    tabStyle: {
      paddingTop: 5,
      paddingBottom: 5,
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
        <TheTabNavigator/>
      </View>
    );
  }
}
