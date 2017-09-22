import React from 'react';
import {Text, View, Image, ScrollView, TouchableOpacity} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

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
      <ScrollView contentContainerStyle={mainStyles.container}>

        <Text style={mainStyles.title}>Buy a textbook!</Text>

        <View style={buyStyles.listItemMainContainer}>
          <TouchableOpacity
            style={buyStyles.listItem}
            onPress={() => navigate('TheBuyDetailsScreen')}
            activeOpacity={100}>
            <Image
              source = {textbook}
              style = {buyStyles.listItemImage}
            />
            <View style={buyStyles.listItemTextContainer}>
              <Text style={buyStyles.listItemCourseCode}>SYSC 4504</Text>
              <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
              <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
              <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
              <View style={buyStyles.listItemColumns}>
                <View style={buyStyles.listItemLeft}>
                  <Text style={buyStyles.listItemPrice}>$150</Text>
                </View>
                <View style={buyStyles.listItemRight}>
                  <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                  <Text style={buyStyles.listItemDate}>2 hours ago</Text>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        <View style={buyStyles.listItemMainContainer}>
          <TouchableOpacity
            style={buyStyles.listItem}
            onPress={() => navigate('TheBuyDetailsScreen')}
            activeOpacity={100}>
            <Image
              source = {textbook}
              style = {buyStyles.listItemImage}
            />
            <View style={buyStyles.listItemTextContainer}>
              <Text style={buyStyles.listItemCourseCode}>SYSC 4504</Text>
              <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
              <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
              <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
              <View style={buyStyles.listItemColumns}>
                <View style={buyStyles.listItemLeft}>
                  <Text style={buyStyles.listItemPrice}>$150</Text>
                </View>
                <View style={buyStyles.listItemRight}>
                  <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                  <Text style={buyStyles.listItemDate}>2 hours ago</Text>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        <View style={buyStyles.listItemMainContainer}>
          <TouchableOpacity
            style={buyStyles.listItem}
            onPress={() => navigate('TheBuyDetailsScreen')}
            activeOpacity={100}>
            <Image
              source = {textbook}
              style = {buyStyles.listItemImage}
            />
            <View style={buyStyles.listItemTextContainer}>
              <Text style={buyStyles.listItemCourseCode}>SYSC 4504</Text>
              <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
              <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
              <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
              <View style={buyStyles.listItemColumns}>
                <View style={buyStyles.listItemLeft}>
                  <Text style={buyStyles.listItemPrice}>$150</Text>
                </View>
                <View style={buyStyles.listItemRight}>
                  <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                  <Text style={buyStyles.listItemDate}>2 hours ago</Text>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        <View style={buyStyles.listItemMainContainer}>
          <TouchableOpacity
            style={buyStyles.listItem}
            onPress={() => navigate('TheBuyDetailsScreen')}
            activeOpacity={100}>
            <Image
              source = {textbook}
              style = {buyStyles.listItemImage}
            />
            <View style={buyStyles.listItemTextContainer}>
              <Text style={buyStyles.listItemCourseCode}>SYSC 4504</Text>
              <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
              <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
              <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
              <View style={buyStyles.listItemColumns}>
                <View style={buyStyles.listItemLeft}>
                  <Text style={buyStyles.listItemPrice}>$150</Text>
                </View>
                <View style={buyStyles.listItemRight}>
                  <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                  <Text style={buyStyles.listItemDate}>2 hours ago</Text>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        <View style={buyStyles.listItemMainContainer}>
          <TouchableOpacity
            style={buyStyles.listItem}
            onPress={() => navigate('TheBuyDetailsScreen')}
            activeOpacity={100}>
            <Image
              source = {textbook}
              style = {buyStyles.listItemImage}
            />
            <View style={buyStyles.listItemTextContainer}>
              <Text style={buyStyles.listItemCourseCode}>SYSC 4504</Text>
              <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
              <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
              <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
              <View style={buyStyles.listItemColumns}>
                <View style={buyStyles.listItemLeft}>
                  <Text style={buyStyles.listItemPrice}>$150</Text>
                </View>
                <View style={buyStyles.listItemRight}>
                  <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                  <Text style={buyStyles.listItemDate}>2 hours ago</Text>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        <View style={buyStyles.listItemMainContainer}>
          <TouchableOpacity
            style={buyStyles.listItem}
            onPress={() => navigate('TheBuyDetailsScreen')}
            activeOpacity={100}>
            <Image
              source = {textbook}
              style = {buyStyles.listItemImage}
            />
            <View style={buyStyles.listItemTextContainer}>
              <Text style={buyStyles.listItemCourseCode}>SYSC 4504</Text>
              <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
              <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
              <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
              <View style={buyStyles.listItemColumns}>
                <View style={buyStyles.listItemLeft}>
                  <Text style={buyStyles.listItemPrice}>$150</Text>
                </View>
                <View style={buyStyles.listItemRight}>
                  <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                  <Text style={buyStyles.listItemDate}>2 hours ago</Text>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>

      </ScrollView>
    );
  }
}

module.exports = BuyScreen;
