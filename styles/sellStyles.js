import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;

const sellStyles = StyleSheet.create ({
  inputFirst: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    paddingLeft: 10,
    paddingTop: 0,
    paddingBottom: 0,
    fontSize: 16,
  },
  input: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    paddingLeft: 10,
    paddingTop: 0,
    paddingBottom: 0,
    fontSize: 16,
  },
  courseInput: {
    width: ScreenWidth*0.40,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    paddingLeft: 10,
    paddingTop: 0,
    paddingBottom: 0,
    fontSize: 16,
  },
  picker: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    alignItems: 'center',
  },
  pickerPlaceholder: {
    width: ScreenWidth*0.70,
    height: 40,
    color: '#B3B3B3',
  },
  pickerSelected: {
    width: ScreenWidth*0.70,
    height: 40,
    color: 'black',
  },
  courseRow: {
    flexDirection: 'row',
  },
  coursePicker: {
    width: ScreenWidth*0.30,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    borderRightColor: 'transparent',
    alignItems: 'center',
  },
  coursePickerPlaceholder: {
    width: ScreenWidth*0.30,
    height: 40,
    color: '#B3B3B3',
  },
  coursePickerSelected: {
    width: ScreenWidth*0.30,
    height: 40,
    color: 'black',
  },
  textbookImage: {
    height: 200,
    width: 200,
  },
  textbookImageBlank: {
    height: 1,
    width: 200,
  },
  textbookImageContainer: {
    marginBottom: 10,
  },
  submitButtonText: {
    color: 'white',
    fontWeight: 'bold',
    width: 70,
    textAlign: 'center',
  },
  redButtonLeft: {
    width: 80,
    height: 30,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#B22222',
    alignItems: 'center',
    justifyContent: 'center',
  },
  redButtonContainer: {
    width: 80,
  },
  sellTitleView: {
    width: ScreenWidth-100,
  },
  sellTitleText: {
    width: ScreenWidth-80,
    fontWeight: 'bold',
    fontSize: 25,
    margin: 5,
    paddingLeft: 5,
  },
});

module.exports = sellStyles;
