import React from 'react';
import {Text, View, Image, TextInput, Picker, TouchableOpacity} from 'react-native';

const styles = require('../styles.js');

class SellScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textbookType: '',
      textbookCondition: '',
    }
  }
  static navigationOptions = {
    tabBarLabel: 'Sell',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {require('./icons/sell.png')}
        style = {[styles.icon, {tintColor: tintColor}]}
      />
    ),
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={styles.container}>

        <Text style={styles.title}>Sell a textbook!</Text>

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
            <Picker.Item label="Condition" value="Condition" />
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
            <Picker.Item label="Type" value="Type" />
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
          <TouchableOpacity onPress={theFunction}>
            <View style={styles.submitButton}>
              <Text style={styles.submitButtonText}>SUBMIT</Text>
            </View>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

function theFunction() {
  alert("Textbook listing has been successfully posted!");
}

module.exports = SellScreen;
