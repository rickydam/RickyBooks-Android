import React from 'react';
import {
  Text,
  View,
  Image,
  TextInput,
  Picker,
  TouchableOpacity,
  ScrollView,
  Keyboard,
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
      textbookTitle: '',
      textbookTitleRef: '',
      textbookAuthor: '',
      textbookAuthorRef: '',
      textbookEdition: '',
      textbookCondition: '',
      textbookType: '',
      textbookCourseCode: '',
      textbookCourseCodeRef: '',
      textbookPrice: '',
      textbookPriceRef: '',
      // textbookImage: '',
    }
    // this.uploadTextbookImage = this.uploadTextbookImage.bind(this);
  }

  // uploadTextbookImage() {
  //   var _this = this;
  //   var ImagePicker = require('react-native-image-picker');
  //   var options = {
  //     title: 'Textbook Image',
  //   };
  //
  //   ImagePicker.showImagePicker(options, (response) => {
  //     let source = {uri: response.uri};
  //     this.setState({
  //       textbookImage: source
  //     });
  //   });
  // }

  static navigationOptions = {
    tabBarLabel: 'Sell',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {sellIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };

  async onSubmit() {
    this.setState({
      textbookEdition: '',
      textbookCondition: '',
      textbookType: '',
    });
    this.textbookTitleRef.clear();
    this.textbookTitleRef.blur();
    this.textbookAuthorRef.clear();
    this.textbookAuthorRef.blur();
    this.textbookCourseCodeRef.clear();
    this.textbookCourseCodeRef.blur();
    this.textbookPriceRef.clear();
    this.textbookPriceRef.blur();

    Keyboard.dismiss();
    try {
      let response = await fetch(
        'http://rickybooks.herokuapp.com/textbooks', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            textbook_title:       this.state.textbookTitle,
            textbook_author:      this.state.textbookAuthor,
            textbook_edition:     this.state.textbookEdition,
            textbook_condition:   this.state.textbookCondition,
            textbook_type:        this.state.textbookType,
            textbook_coursecode:  this.state.textbookCourseCode,
            textbook_price:       this.state.textbookPrice
          })
        });
        let res = await response.text();
        if(response.status>=200 && response.status<300) {
          alert("Success yayyy!!!!");
        }
        else {
          alert("Didn't work... awhhh..");
        }
    } catch(error) {
      alert("error: " + error);
    }
  }

  render() {
    const keyboardOffset = 25;
    return (
      // <KeyboardAvoidingView
      //   behavior='position'
      //   keyboardVerticalOffset={keyboardOffset}>
      //
      //   <ScrollView
      //     contentContainerStyle={mainStyles.container}
      //     keyboardShouldPersistTaps='handled'>
        <View style={mainStyles.container}>

          <Text style={mainStyles.title}>Sell a textbook!</Text>

          {/* <TouchableOpacity onPress={() => this.uploadTextbookImage()}>
            <View style={mainStyles.blueButtonBig}>
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
          </View> */}

          <TextInput
            style={sellStyles.inputFirst}
            placeholder="Title"
            placeholderTextColor="#B3B3B3"
            underlineColorAndroid="transparent"
            autoCapitalize="words"
            onChangeText={(text) => this.setState({textbookTitle:text})}
            ref={input => {this.textbookTitleRef=input}}
            maxLength={100}
          />

          <TextInput
            style={sellStyles.input}
            placeholder="Author"
            placeholderTextColor="#B3B3B3"
            underlineColorAndroid="transparent"
            autoCapitalize="words"
            onChangeText={(text) => this.setState({textbookAuthor:text})}
            ref={input => {this.textbookAuthorRef=input}}
          />

          <View style={sellStyles.picker}>
            <Picker
              mode="dropdown"
              style={
                this.state.textbookEdition == '' ?
                sellStyles.pickerPlaceholder :
                sellStyles.pickerSelected}
              selectedValue={this.state.textbookEdition}
              onValueChange={(itemValue, itemIndex) => this.setState({textbookEdition: itemValue})}>
              <Picker.Item label="Edition" value="" />
              <Picker.Item label="Custom Edition" value="Custom Edition" />
              <Picker.Item label="Global Edition" value="Global Edition" />
              <Picker.Item label="1st Edition" value="1st Edition" />
              <Picker.Item label="2nd Edition" value="2nd Edition" />
              <Picker.Item label="3rd Edition" value="3rd Edition" />
              <Picker.Item label="4th Edition" value="4th Edition" />
              <Picker.Item label="5th Edition" value="5th Edition" />
              <Picker.Item label="6th Edition" value="6th Edition" />
              <Picker.Item label="7th Edition" value="7th Edition" />
              <Picker.Item label="8th Edition" value="8th Edition" />
              <Picker.Item label="9th Edition" value="9th Edition" />
              <Picker.Item label="10th Edition" value="10th Edition" />
              <Picker.Item label="11th Edition" value="11th Edition" />
              <Picker.Item label="12th Edition" value="12th Edition" />
              <Picker.Item label="13th Edition" value="13th Edition" />
              <Picker.Item label="14th Edition" value="14th Edition" />
              <Picker.Item label="15th Edition" value="15th Edition" />
            </Picker>
          </View>

          <View style={sellStyles.picker}>
            <Picker
              mode="dropdown"
              style={
                this.state.textbookCondition == '' ?
                sellStyles.pickerPlaceholder :
                sellStyles.pickerSelected}
              selectedValue={this.state.textbookCondition}
              onValueChange={(itemValue, itemIndex) => this.setState({textbookCondition: itemValue})}>
              <Picker.Item label="Condition" value="" />
              <Picker.Item label="New" value="New" />
              <Picker.Item label="Like New" value="Like New" />
              <Picker.Item label="Good" value="Good" />
              <Picker.Item label="Okay" value="Okay" />
            </Picker>
          </View>

          <View style={sellStyles.picker}>
            <Picker
              mode="dropdown"
              style={
                this.state.textbookType == '' ?
                sellStyles.pickerPlaceholder :
                sellStyles.pickerSelected}
              itemStyle={sellStyles.testPickerItem}
              selectedValue={this.state.textbookType}
              onValueChange={(itemValue) => this.setState({textbookType: itemValue})}>
              <Picker.Item label="Type" value="" />
              <Picker.Item label="Paperback" value="Paperback" />
              <Picker.Item label="Hardcover" value="Hardcover" />
              <Picker.Item label="Looseleaf" value="Looseleaf" />
            </Picker>
          </View>

          <TextInput
            style={sellStyles.input}
            placeholder="Course Code (ABCD1234)"
            placeholderTextColor="#B3B3B3"
            underlineColorAndroid="transparent"
            autoCapitalize="characters"
            maxLength={8}
            onChangeText={(text) => this.setState({textbookCourseCode:text})}
            ref={input => {this.textbookCourseCodeRef=input}}
          />

          <TextInput
            style={sellStyles.input}
            placeholder="$ Price"
            placeholderTextColor="#B3B3B3"
            underlineColorAndroid="transparent"
            keyboardType="numeric"
            maxLength={6}
            onChangeText={(text) => this.setState({textbookPrice:text})}
            ref={input => {this.textbookPriceRef=input}}
          />

          <View style={sellStyles.submitContainer}>
            <TouchableOpacity onPress={this.onSubmit.bind(this)}>
              <View style={mainStyles.purpleButton}>
                <Text style={mainStyles.buttonText}>SUBMIT</Text>
              </View>
            </TouchableOpacity>
          </View>

        </View>
        /* </ScrollView>
      </KeyboardAvoidingView> */
    );
  }
}

module.exports = SellScreen;
