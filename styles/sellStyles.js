import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get("window").width;

const sellStyles = StyleSheet.create ({
  input: {
    width: ScreenWidth*0.70,
    height: 45,
    borderWidth: 1,
    borderColor: 'lightgray',
    paddingLeft: 10,
    fontSize: 16,
  },
  textbookCondition: {
    width: ScreenWidth*0.70,
    height: 45,
    borderWidth: 1,
    borderColor: 'lightgray',
    alignItems: 'center',
  },
  textbookConditionPicker: {
    width: ScreenWidth*0.68,
    height: 40,
    color: 'gray',
  },
  textbookType: {
    width: ScreenWidth*0.70,
    height: 45,
    borderWidth: 1,
    borderColor: 'lightgray',
    alignItems: 'center',
  },
  textbookTypePicker: {
    width: ScreenWidth*0.68,
    height: 40,
    color: 'gray',
  },
  submitButton: {
    width: 70,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#9932CD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  submitButtonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  uploadButton: {
    width: 200,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  uploadButtonText: {
    color: 'white',
    fontWeight: 'bold',
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
