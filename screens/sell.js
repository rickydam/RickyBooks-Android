import React from 'react';
import {Text, View, Image, TextInput, Picker, TouchableOpacity, ScrollView} from 'react-native';
import ImagePicker from 'react-native-image-picker';

const styles = require('../styles.js');
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
        style = {[styles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <ScrollView contentContainerStyle={styles.container}>

        <Text style={styles.title}>Sell a textbook!</Text>

        <TouchableOpacity onPress={() => this.uploadTextbookImage()}>
          <View style={styles.uploadButton}>
            <Text style={styles.uploadButtonText}>UPLOAD TEXTBOOK IMAGE</Text>
          </View>
        </TouchableOpacity>

        <View style={styles.textbookImageContainer}>
          <Image
            source={
              this.state.textbookImage != '' ?
              this.state.textbookImage :
              placeholderImg}
            style={
              this.state.textbookImage != '' ?
              styles.textbookImage :
              styles.textbookImageBlank} />
        </View>

        <TextInput
          style={styles.input}
          placeholder="Title"
          underlineColorAndroid="transparent"
        />

        <TextInput
          style={styles.input}
          placeholder="Author"
          underlineColorAndroid="transparent"
        />

        <TextInput
          style={styles.input}
          placeholder="Edition"
          underlineColorAndroid="transparent"
        />

        <View style={styles.textbookCondition}>
          <Picker
            style={styles.textbookConditionPicker}
            itemTextStyle={styles.testPickerItem}
            selectedValue={this.state.textbookCondition}
            onValueChange={(itemValue, itemIndex) => this.setState({textbookCondition: itemValue})}>
            <Picker.Item label="Condition" value="" />
            <Picker.Item label="Like New" value="Like New" />
            <Picker.Item label="Good" value="Good" />
            <Picker.Item label="Okay" value="Okay" />
          </Picker>
        </View>

        <View style={styles.textbookType}>
          <Picker
            style={styles.textbookTypePicker}
            itemStyle={styles.testPickerItem}
            selectedValue={this.state.textbookType}
            onValueChange={(itemValue, itemIndex) => this.setState({textbookType: itemValue})}>
            <Picker.Item label="Type" value="" />
            <Picker.Item label="Paperback" value="Paperback" />
            <Picker.Item label="Hardcover" value="Hardcover" />
            <Picker.Item label="Looseleaf" value="Looseleaf" />
          </Picker>
        </View>

        <TextInput
          style={styles.input}
          placeholder="Course Code"
          underlineColorAndroid="transparent"
        />

        <View style={styles.submitContainer}>
          <TouchableOpacity onPress={() => checkConditionValue(this.state.textbookCondition)}>
            <View style={styles.submitButton}>
              <Text style={styles.submitButtonText}>SUBMIT</Text>
            </View>
          </TouchableOpacity>
        </View>

      </ScrollView>
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
