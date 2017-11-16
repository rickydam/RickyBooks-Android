import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;

const sellStyles = StyleSheet.create ({
  input: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    paddingLeft: 10,
    fontSize: 16,
  },
  courseInput: {
    width: ScreenWidth*0.40,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    paddingLeft: 10,
    paddingBottom: 9,
    fontSize: 16,
  },
  inputFirst: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    paddingLeft: 10,
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
});

module.exports = sellStyles;
