import React from 'react';
import {Text, View, Image, TouchableOpacity, TextInput} from 'react-native';
import Modal from 'react-native-modal';

const mainStyles = require('../styles/mainStyles.js');
const buyDetailsStyles = require('../styles/buyDetailsStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

class BuyDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      values: '',
    }
  }

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

  componentDidMount() {
    this.setState({
      values: this.props.navigation.state.params
    });
  }

  render() {
    return (
      <View style={mainStyles.detailsContainer}>
        <Text style={buyDetailsStyles.itemTitle}>{this.state.values["title"]}</Text>
        <Text style={buyDetailsStyles.itemText}>{this.state.values["author"]}</Text>
        <Image
          source={textbook}
          style={buyDetailsStyles.itemImage}
        />
        <View style={buyDetailsStyles.itemColumns}>
          <View style={buyDetailsStyles.itemColumnLeft}>
            <Text style={buyDetailsStyles.itemText}>{this.state.values["edition"]}</Text>
            <Text style={buyDetailsStyles.itemText}>{this.state.values["condition"]}</Text>
            <Text style={buyDetailsStyles.itemText}>{this.state.values["type"]}</Text>
            <Text style={buyDetailsStyles.itemText}>{this.state.values["coursecode"]}</Text>
          </View>
          <View style={buyDetailsStyles.itemColumnRight}>
            <Text style={buyDetailsStyles.itemSeller}>Ricky Dam</Text>
            <Text style={buyDetailsStyles.itemDate}>{this.state.values["created_at"]}</Text>
            <Text style={buyDetailsStyles.itemPrice}>${this.state.values["price"]}</Text>
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

module.exports = BuyDetails;
