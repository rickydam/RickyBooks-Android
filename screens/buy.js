import React from 'react';
import {Text, View, Image, ScrollView} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');

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

        <View style={buyStyles.listItem}>
          <Image
            source = {require('../images/textbook.jpg')}
            style = {buyStyles.listItemImage}
          />
          <View style={buyStyles.listItemTextContainer}>
            <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
            <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
            <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
            <Text style={buyStyles.listItemPrice}>$150</Text>
            <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.listItemDate}>2 hours ago</Text>
          </View>
        </View>

        <View style={buyStyles.listItem}>
          <Image
            source = {require('../images/textbook.jpg')}
            style = {buyStyles.listItemImage}
          />
          <View style={buyStyles.listItemTextContainer}>
            <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
            <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
            <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
            <Text style={buyStyles.listItemPrice}>$150</Text>
            <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.listItemDate}>2 hours ago</Text>
          </View>
        </View>

        <View style={buyStyles.listItem}>
          <Image
            source = {require('../images/textbook.jpg')}
            style = {buyStyles.listItemImage}
          />
          <View style={buyStyles.listItemTextContainer}>
            <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
            <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
            <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
            <Text style={buyStyles.listItemPrice}>$150</Text>
            <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.listItemDate}>2 hours ago</Text>
          </View>
        </View>

        <View style={buyStyles.listItem}>
          <Image
            source = {require('../images/textbook.jpg')}
            style = {buyStyles.listItemImage}
          />
          <View style={buyStyles.listItemTextContainer}>
            <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
            <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
            <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
            <Text style={buyStyles.listItemPrice}>$150</Text>
            <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.listItemDate}>2 hours ago</Text>
          </View>
        </View>

        <View style={buyStyles.listItem}>
          <Image
            source = {require('../images/textbook.jpg')}
            style = {buyStyles.listItemImage}
          />
          <View style={buyStyles.listItemTextContainer}>
            <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
            <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
            <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
            <Text style={buyStyles.listItemPrice}>$150</Text>
            <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.listItemDate}>2 hours ago</Text>
          </View>
        </View>

        <View style={buyStyles.listItem}>
          <Image
            source = {require('../images/textbook.jpg')}
            style = {buyStyles.listItemImage}
          />
          <View style={buyStyles.listItemTextContainer}>
            <Text style={buyStyles.listItemTitle}>Fundamentals of Web Development</Text>
            <Text style={buyStyles.listItemAuthor}>Randy Connolly, Ricardo Hoar</Text>
            <Text style={buyStyles.listItemEdition}>2nd Edition</Text>
            <Text style={buyStyles.listItemPrice}>$150</Text>
            <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.listItemDate}>2 hours ago</Text>
          </View>
        </View>

      </ScrollView>
    );
  }
}

module.exports = BuyScreen;
