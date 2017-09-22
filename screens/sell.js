import React from 'react';
import {
  Text,
  View,
  Image,
  TextInput,
  Picker,
  TouchableOpacity,
  ScrollView,
  KeyboardAvoidingView
} from 'react-native';
import ImagePicker from 'react-native-image-picker';

const mainStyles = require('../styles/mainStyles.js');
const sellStyles = require('../styles/sellStyles.js');
const sellIcon = require('../images//icons/sell.png');
const placeholderImg = require('../images/placeholder.png')

class SellScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textbookType: '',
      textbookCondition: '',
      textbookImage: '',
    }
    this.uploadTextbookImage = this.uploadTextbookImage.bind(this);
  }

  uploadTextbookImage() {
    var _this = this;
    var ImagePicker = require('react-native-image-picker');
    var options = {
      title: 'Textbook Image',
    };

    ImagePicker.showImagePicker(options, (response) => {
      let source = {uri: response.uri};
      this.setState({
        textbookImage: source
      });
    });
  }
  static navigationOptions = {
    tabBarLabel: 'Sell',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {sellIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    const keyboardOffset = 25;
    return (
      <KeyboardAvoidingView
        behavior='position'
        keyboardVerticalOffset={keyboardOffset}>

        <ScrollView contentContainerStyle={mainStyles.container}>

          <Text style={mainStyles.title}>Sell a textbook!</Text>

          <TouchableOpacity onPress={() => this.uploadTextbookImage()}>
            <View style={mainStyles.blueButton}>
              <Text style={mainStyles.buttonText}>UPLOAD TEXTBOOK IMAGE</Text>
            </View>
          </TouchableOpacity>

          <View style={sellStyles.textbookImageContainer}>
            <Image
              source={
                this.state.textbookImage != '' ?
                this.state.textbookImage :
                placeholderImg}
              style={
                this.state.textbookImage != '' ?
                sellStyles.textbookImage :
                sellStyles.textbookImageBlank} />
          </View>

          <TextInput
            style={sellStyles.inputFirst}
            placeholder="Title"
            placeholderTextColor="lightgray"
            underlineColorAndroid="transparent"
          />

          <TextInput
            style={sellStyles.input}
            placeholder="Author"
            placeholderTextColor="lightgray"
            underlineColorAndroid="transparent"
          />

          <TextInput
            style={sellStyles.input}
            placeholder="Edition"
            placeholderTextColor="lightgray"
            underlineColorAndroid="transparent"
          />

          <View style={sellStyles.textbookCondition}>
            <Picker
              mode="dropdown"
              style={
                this.state.textbookCondition == '' ?
                sellStyles.textbookConditionPickerPlaceholder :
                sellStyles.textbookConditionPickerSelected}
              selectedValue={this.state.textbookCondition}
              onValueChange={(itemValue, itemIndex) => this.setState({textbookCondition: itemValue})}>
              <Picker.Item label="Condition" value="" />
              <Picker.Item label="Like New" value="Like New" />
              <Picker.Item label="Good" value="Good" />
              <Picker.Item label="Okay" value="Okay" />
            </Picker>
          </View>

          <View style={sellStyles.textbookType}>
            <Picker
              mode="dropdown"
              style={
                this.state.textbookType == '' ?
                sellStyles.textbookTypePickerPlaceholder :
                sellStyles.textbookTypePickerSelected}
              itemStyle={sellStyles.testPickerItem}
              selectedValue={this.state.textbookType}
              onValueChange={(itemValue, itemIndex) => this.setState({textbookType: itemValue})}>
              <Picker.Item label="Type" value="" />
              <Picker.Item label="Paperback" value="Paperback" />
              <Picker.Item label="Hardcover" value="Hardcover" />
              <Picker.Item label="Looseleaf" value="Looseleaf" />
            </Picker>
          </View>

          <TextInput
            style={sellStyles.input}
            placeholder="Course Code"
            placeholderTextColor="lightgray"
            underlineColorAndroid="transparent"
          />

          <View style={sellStyles.submitContainer}>
            <TouchableOpacity onPress={() => checkConditionValue(this.state.textbookCondition)}>
              <View style={mainStyles.purpleButton}>
                <Text style={mainStyles.buttonText}>SUBMIT</Text>
              </View>
            </TouchableOpacity>
          </View>

        </ScrollView>
      </KeyboardAvoidingView>
    );
  }
}

function checkConditionValue(textbookCondition) {
  if(textbookCondition == '') {
    alert("Please select a textbook condition.");
  }
  else {
    alert(textbookCondition);
  }
}

module.exports = SellScreen;
