import React from 'react';
import {Text, View, Image, ScrollView, TouchableOpacity} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const messagesStyles = require('../styles/messagesStyles.js');
const messagesIcon = require('../images/icons/messages.png');
const ricky = require('../images/ricky.jpg');

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
      <ScrollView contentContainerStyle={mainStyles.container}>
        <Text style={mainStyles.title}>Messages</Text>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style = {messagesStyles.messagesContainer}
          onPress = {() => navigate('TheMessagesDetailsScreen')}
          activeOpacity = {100}
          >
          <View style={messagesStyles.columns}>
            <View style={messagesStyles.leftColumn}>
              <Image
                source = {ricky}
                style = {messagesStyles.userImage}
              />
            </View>
            <View style={messagesStyles.rightColumn}>
              <View style={messagesStyles.nameDate}>
                <Text style={messagesStyles.listName}>Ricky Dam</Text>
                <Text style={messagesStyles.listDate}>1 hour ago</Text>
              </View>

              <Text style={messagesStyles.listPreview}>
                Hi, is the textbook still available?
                I would like to buy it.
                When can we meet up?
              </Text>
            </View>
          </View>
        </TouchableOpacity>

      </ScrollView>
    );
  }
}

module.exports = MessagesScreen
