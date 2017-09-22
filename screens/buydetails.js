import React from 'react';
import {Text, View, Image, TouchableOpacity} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

class BuyDetails extends React.Component {
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
      <View style={mainStyles.detailsContainer}>
        <Image
          source={textbook}
          style={buyStyles.itemImage}
        />
        <Text style={buyStyles.itemTitle}>Fundamentals of Web Development</Text>
        <Text style={buyStyles.itemAuthor}>Randy Connolly, Ricardo Hoar</Text>
        <Text style={buyStyles.itemEdition}>2nd Edition</Text>
        <View style={buyStyles.itemColumns}>
          <View style={buyStyles.itemColumn}>
            <Text style={buyStyles.itemPrice}>$150</Text>
          </View>
          <View style={buyStyles.itemColumn}>
            <Text style={buyStyles.itemSeller}>Ricky Dam</Text>
            <Text style={buyStyles.itemDate}>2 hours ago</Text>
          </View>
        </View>
        <TouchableOpacity>
          <View style={mainStyles.blueButtonSmall}>
            <Text style={mainStyles.buttonText}>MESSAGE</Text>
          </View>
        </TouchableOpacity>
      </View>
    );
  }
}

module.exports = BuyDetails
