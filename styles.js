import {StyleSheet, Dimensions} from 'react-native';

const ScreenHeight = Dimensions.get("window").height;
const ScreenWidth = Dimensions.get("window").width;

const styles = StyleSheet.create ({
  container: {
    backgroundColor: '#fff',
    alignItems: 'center',
    marginTop: 10,
    paddingBottom: 20,
  },
  tabContainer: {
    height: ScreenHeight-20,
    backgroundColor: 'white',
  },
  icon: {
    width: 26,
    height: 26,
  },
  input: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    margin: 5,
    paddingLeft: 10,
  },
  title: {
    fontWeight: 'bold',
    fontSize: 25,
    margin: 10,
  },
  textbookCondition: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    margin: 5,
    alignItems: 'center',
  },
  textbookConditionPicker: {
    width: ScreenWidth*0.68,
    height: 40,
  },
  textbookType: {
    width: ScreenWidth*0.70,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    margin: 5,
    alignItems: 'center',
  },
  textbookTypePicker: {
    width: ScreenWidth*0.68,
    height: 40,
    alignItems: 'center',
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
    margin: 10,
  },
});

module.exports = styles;
