import React from 'react';
import {Text, View, Image} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const messagesIcon = require('../images/icons/messages.png');

class MessagesScreen extends React.Component {
  static navigationOptions = {
    tabBarLabel: 'Messages',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {messagesIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={mainStyles.container}>
        <Text>This is the messages screen.</Text>
      </View>
    );
  }
}

module.exports = MessagesScreen
