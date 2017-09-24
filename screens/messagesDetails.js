import React from 'react';
import {Text, View, Image, ScrollView} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const messagesIcon = require('../images/icons/messages.png');

class MessagesDetails extends React.Component {
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
    return (
      <View style={mainStyles.detailsContainer}>
        <ScrollView contentContainerStyle={mainStyles.container}>
          <Text>This is the MessageDetails screen.</Text>
        </ScrollView>
      </View>
    );
  }
}

module.exports = MessagesDetails;
