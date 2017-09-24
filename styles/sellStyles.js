import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;

const sellStyles = StyleSheet.create ({
  input: {
    width: ScreenWidth*0.70,
    height: 45,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    paddingLeft: 10,
    fontSize: 16,
  },
  inputFirst: {
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
    borderTopColor: 'transparent',
    alignItems: 'center',
  },
  textbookConditionPickerPlaceholder: {
    width: ScreenWidth*0.68,
    height: 40,
    color: 'lightgray',
  },
  textbookConditionPickerSelected: {
    width: ScreenWidth*0.68,
    height: 40,
    color: 'black',
  },
  textbookType: {
    width: ScreenWidth*0.70,
    height: 45,
    borderWidth: 1,
    borderColor: 'lightgray',
    borderTopColor: 'transparent',
    alignItems: 'center',
  },
  textbookTypePickerPlaceholder: {
    width: ScreenWidth*0.68,
    height: 40,
    color: 'lightgray',
  },
  textbookTypePickerSelected: {
    width: ScreenWidth*0.68,
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
