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
  KeyboardAvoidingView,
  Alert,
  FlatList
} from 'react-native';
import ImagePicker from 'react-native-image-picker';

const mainStyles = require('../styles/mainStyles.js');
const sellStyles = require('../styles/sellStyles.js');
const sellIcon = require('../images//icons/sell.png');
const placeholderImg = require('../images/placeholder.png');

var coursetypes = [
  "Course", "ACCT", "AERO", "AFRI", "ALDS", "ANTH", "ARAB", "ARCC", "ARCH",
    "ARCN", "ARCS", "ARCT", "ARCU", "ARTH", "ASLA", "BIT",  "BIOC", "BIOL",
    "BIOM", "BUSI", "CCDP", "CDNS", "CGSC", "CIED", "CIVE", "CHEM", "CHIN",
    "CHST", "CIVJ", "CLCV", "CLMD", "COMP", "COMS", "COOP", "CRCJ", "CURA",
    "DATA", "DBST", "DIGH", "DIST", "EACJ", "ECON", "ELEC", "ERTH", "ESPW",
    "ECOR", "ENGL", "ENSC", "ENST", "ENVE", "ENVJ", "EPAF", "ESLA", "EURR",
    "FOOD", "FILM", "FINA", "FINS", "FREN", "FYSM", "GEOG", "GEOM", "GERM",
    "GINS", "GPOL", "GREK", "HCIN", "HIST", "HLTH", "HUMR", "HUMS", "IBUS",
    "IDES", "IMD",  "INAF", "INDG", "INSC", "IPAF", "IPIS", "IRM",  "ISCI",
    "ISYS", "ITEC", "ITIS", "IDMG", "LANG", "LATN", "ITAL", "JAPA", "JOUR",
    "KORE", "LACS", "LAWS", "LING", "MAAE", "MAAJ", "MATH", "MECH", "MEMS",
    "MGDS", "MGMT", "MKTG", "MUSI", "NET",  "NEUR", "NRTH", "NSCI", "PANL",
    "PADM", "PAPM", "PECO", "PHIL", "PHYJ", "PHYS", "PLT",  "POLM", "PORT",
    "PSCI", "PSYC", "RELI", "RUSS", "SAST", "SERG", "SOCI", "SOWK", "SPAN",
    "SREE", "STAT", "STGY", "SXST", "SYSC", "TIMG", "TOMS", "TSES", "WGST"
];

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
      textbookCoursePart1: '',
      textbookCoursePart1Ref: '',
      textbookCoursePart2: '',
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

  clearInputs() {
    this.setState({
      textbookTitle: '',
      textbookTitleRef: '',
      textbookAuthor: '',
      textbookAuthorRef: '',
      textbookEdition: '',
      textbookCondition: '',
      textbookType: '',
      textbookCoursePart1: '',
      textbookCoursePart1Ref: '',
      textbookCoursePart2: '',
      textbookPrice: '',
      textbookPriceRef: '',
    });
    this.textbookTitleRef.clear();
    this.textbookTitleRef.blur();
    this.textbookAuthorRef.clear();
    this.textbookAuthorRef.blur();
    this.textbookCoursePart2Ref.clear();
    this.textbookCoursePart2Ref.blur();
    this.textbookPriceRef.clear();
    this.textbookPriceRef.blur();
  }

  async onSubmit() {
    var coursecode;
    if(this.state.textbookCoursePart1 == '' | this.state.textbookCoursePart2 == '') {
      coursecode = '';
    }
    else {
      coursecode = this.state.textbookCoursePart1 + this.state.textbookCoursePart2;
    }
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
            textbook_coursecode:  coursecode,
            textbook_price:       this.state.textbookPrice
          })
        });
        let res = await response.text();
        if(response.status>=200 && response.status<300) {
          this.clearInputs();
          alert("Successfully posted.");
        }
        else {
          resdata = JSON.parse(res).data;
          var thekeys = Object.keys(resdata);
          var theString = "";
          for(i=0; i<thekeys.length; i++) {
            thekeys[i] = thekeys[i].replace("textbook_", "Textbook ");
            if(thekeys[i] == "Textbook title") {
              thekeys[i] = thekeys[i].replace("title", "Title");
            }
            if(thekeys[i] == "Textbook author") {
              thekeys[i] = thekeys[i].replace("author", "Author");
            }
            if(thekeys[i] == "Textbook edition") {
              thekeys[i] = thekeys[i].replace("edition", "Edition");
            }
            if(thekeys[i] == "Textbook condition") {
              thekeys[i] = thekeys[i].replace("condition", "Condition");
            }
            if(thekeys[i] == "Textbook type") {
              thekeys[i] = thekeys[i].replace("type", "Type");
            }
            if(thekeys[i] == "Textbook coursecode") {
              thekeys[i] = thekeys[i].replace("coursecode", "Course Code");
            }
            if(thekeys[i] == "Textbook price") {
              thekeys[i] = thekeys[i].replace("price", "Price");
            }
          }
          for(i=0; i<thekeys.length; i++) {
            if(i==thekeys.length-1) {
              theString += "Missing: " + thekeys[i];
            }
            else {
              theString += "Missing: " + thekeys[i] + "\n";
            }
          }
          Alert.alert("All fields are required", theString);
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
              onValueChange={(itemValue, itemIndex) => this.setState({textbookEdition:itemValue})}>
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
              onValueChange={(itemValue, itemIndex) => this.setState({textbookCondition:itemValue})}>
              <Picker.Item label="Condition" value="" />
              <Picker.Item label="New" value="New" />
              <Picker.Item label="Like New" value="Like New" />
              <Picker.Item label="Good" value="Good" />
              <Picker.Item label="Okay" value="Okay" />
              <Picker.Item label="Bad" value="Bad" />
            </Picker>
          </View>

          <View style={sellStyles.picker}>
            <Picker
              mode="dropdown"
              style={
                this.state.textbookType == '' ?
                sellStyles.pickerPlaceholder :
                sellStyles.pickerSelected}
              selectedValue={this.state.textbookType}
              onValueChange={(itemValue) => this.setState({textbookType:itemValue})}>
              <Picker.Item label="Type" value="" />
              <Picker.Item label="Paperback" value="Paperback" />
              <Picker.Item label="Hardcover" value="Hardcover" />
              <Picker.Item label="Looseleaf" value="Looseleaf" />
            </Picker>
          </View>

          <View style={sellStyles.courseRow}>

            <View style={sellStyles.coursePicker}>
              <Picker
                mode="dropdown"
                style={
                  this.state.textbookCoursePart1 == '' ?
                  sellStyles.coursePickerPlaceholder :
                  sellStyles.coursePickerSelected}
                selectedValue={this.state.textbookCoursePart1}
                onValueChange={(itemValue) => this.setState({textbookCoursePart1:itemValue})}>
                {
                  coursetypes.map((item, index) => {
                    if(item == "Course") {
                      return (
                        <Picker.Item key={item} label={item} value="" />
                      );
                    }
                    else {
                      return (
                        <Picker.Item key={item} label={item} value={item} />
                      );
                    }
                  })
                }
              </Picker>
            </View>

            <TextInput
              style={sellStyles.courseInput}
              placeholder="Code (####)"
              placeholderTextColor="#B3B3B3"
              underlineColorAndroid="transparent"
              autoCapitalize="characters"
              keyboardType="numeric"
              maxLength={4}
              onChangeText={(text) => this.setState({textbookCoursePart2:text})}
              ref={input => {this.textbookCoursePart2Ref=input}}
            />

          </View>

          <TextInput
            style={sellStyles.input}
            placeholder="$ Price"
            placeholderTextColor="#B3B3B3"
            underlineColorAndroid="transparent"
            keyboardType="numeric"
            maxLength={3}
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
