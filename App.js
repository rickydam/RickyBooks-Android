import React from 'react';
import {View} from 'react-native';
import {TabNavigator} from 'react-navigation';

const styles = require('./styles.js');
const HomeScreen = require('./screens/home.js');
const BuyScreen = require('./screens/buy.js');
const SellScreen = require('./screens/sell.js');
const ProfileScreen = require('./screens/profile.js');

const AppNavigator = TabNavigator ({
  TheHomeScreen: {screen: HomeScreen},
  TheBuyScreen: {screen: BuyScreen},
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
    iconStyle: {
      paddingTop: 30,
    },
    showIcon: true,
    activeTintColor: 'black',
    inactiveTintColor: 'lightgray',
  },
});

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.tabContainer}>
        <AppNavigator/>
      </View>
    );
  }
}
