import React from 'react';
import {Text, View, Image, TouchableOpacity, TextInput} from 'react-native';
import Modal from 'react-native-modal';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

class BuyDetails extends React.Component {
  state = {
    isModalVisible: false,
  }
  _showModal = () => this.setState({isModalVisible: true})
  _hideModal = () => this.setState({isModalVisible: false})

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
        <TouchableOpacity onPress={this._showModal}>
          <View style={mainStyles.blueButtonMedium}>
            <Text style={mainStyles.buttonText}>MESSAGE</Text>
          </View>
        </TouchableOpacity>
        <Modal
          style={mainStyles.modalContainer}
          isVisible={this.state.isModalVisible}
          backdropOpacity={0.8}
          onBackButtonPress={this._hideModal}
          >
          <TouchableOpacity style={mainStyles.redCloseButton} onPress={this._hideModal}>
            <Text style={mainStyles.buttonTextLarge}>X</Text>
          </TouchableOpacity>
          <View style={mainStyles.textAreaContainer}>
            <TextInput
              style={mainStyles.textArea}
              multiline={true}
              numberOfLines={2}
              textAlignVertical="top"
              autoCapitalize="words"
              placeholder="Message..."
              onChangeText={(text) => this.setState({text})}
              value={this.state.text} />
            <TouchableOpacity
              style={mainStyles.blueButtonSmall}
              onPress={() => alert("Message has been sent successfully!")}
              >
              <Text style={mainStyles.buttonText}>SEND</Text>
            </TouchableOpacity>
          </View>
        </Modal>
      </View>
    );
  }
}

module.exports = BuyDetails
