import React from 'react';
import {Text, View, Image, TouchableOpacity, TextInput} from 'react-native';
import Modal from 'react-native-modal';
import PhotoView from 'react-native-photo-view';

const mainStyles = require('../styles/mainStyles.js');
const buyDetailsStyles = require('../styles/buyDetailsStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

class BuyDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      values: '',
      messageModal: false,
      imageModal: false,
    }
  }

  showMessageModal = () => this.setState({messageModal:true});
  hideMessageModal = () => this.setState({messageModal:false});

  showImageModal = () => this.setState({imageModal:true});
  hideImageModal = () => this.setState({imageModal:false});

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
      <View style={buyDetailsStyles.detailsContainer}>

        <Text style={buyDetailsStyles.itemTitle}>{this.state.values["title"]}</Text>
        <Text style={buyDetailsStyles.itemText}>{this.state.values["author"]}</Text>

        <TouchableOpacity onPress={this.showImageModal}>
          <Image
            source={textbook}
            style={buyDetailsStyles.itemImage}
          />
        </TouchableOpacity>

        <Modal
          isVisible={this.state.imageModal}
          onBackButtonPress={this.hideImageModal}
          onBackdropPress={this.hideImageModal}
          backdropTransitionInTiming={1}
          backdropTransitionOutTiming={1}
          animationIn="fadeIn"
          animationInTiming={1}
          animationOut="fadeOut"
          animationOutTiming={1}>
          <View style={buyDetailsStyles.imageModalView}>
            <PhotoView
              source={textbook}
              style={buyDetailsStyles.modalImage}>
            </PhotoView>
          </View>
        </Modal>

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

        <TouchableOpacity onPress={this.showMessageModal}>
          <View style={mainStyles.blueButtonMedium}>
            <Text style={buyDetailsStyles.messageButtonText}>MESSAGE</Text>
          </View>
        </TouchableOpacity>

        <Modal
          style={buyDetailsStyles.messageModalContainer}
          isVisible={this.state.messageModal}
          backdropOpacity={0.70}
          onBackButtonPress={this.hideMessageModal}
          >
          <TouchableOpacity style={mainStyles.redCloseButton} onPress={this.hideMessageModal}>
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
              <Text style={buyDetailsStyles.sendButtonText}>SEND</Text>
            </TouchableOpacity>
          </View>
        </Modal>

      </View>
    );
  }
}

module.exports = BuyDetails;
