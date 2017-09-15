import {StyleSheet, Dimensions} from 'react-native';

const ScreenHeight = Dimensions.get("window").height;

const styles = StyleSheet.create ({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  tabContainer: {
    height: ScreenHeight,
    backgroundColor: 'white',
  },
  icon: {
    width: 26,
    height: 26,
  },
  input: {
    width: 250,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    margin: 5,
    textAlign: 'center',
  },
  title: {
    fontWeight: 'bold',
    fontSize: 25,
    margin: 10,
  },
  textbookCondition: {
    width: 250,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    margin: 5,
    alignItems: 'center',
  },
  textbookConditionPicker: {
    width: 110,
    height: 40,
    marginLeft: 30,
  },
  textbookType: {
    width: 250,
    height: 40,
    borderWidth: 1,
    borderColor: 'lightgray',
    margin: 5,
    alignItems: 'center',
  },
  textbookTypePicker: {
    width: 120,
    height: 40,
    marginLeft: 25,
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
});

module.exports = styles;
