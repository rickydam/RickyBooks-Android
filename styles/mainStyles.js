import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;
const ScreenHeight = Dimensions.get('window').height;

const mainStyles = StyleSheet.create ({
  container: {
    backgroundColor: 'white',
    alignItems: 'center',
    paddingLeft: 5,
    paddingRight: 5,
    width: ScreenWidth,
    height: ScreenHeight-88,
  },
  tabContainer: {
    height: ScreenHeight-20,
    backgroundColor: 'white',
  },
  icon: {
    width: 20,
    height: 20,
  },
  mediumImage: {
    width: 200,
    height: 200,
  },
  title: {
    width: ScreenWidth,
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 25,
    margin: 5,
    alignSelf: 'center',
  },
  blueButtonBig: {
    width: 200,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  blueButtonMedium: {
    width: 100,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  blueButtonSmall: {
    width: 60,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
    alignSelf: 'center',
  },
  blueButtonSmallHC: {
    width: 60,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
    alignSelf: 'center',
  },
  purpleButton: {
    width: 70,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#9932CD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  purpleButtonSmall: {
    width: 60,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#9932CD',
    alignItems: 'center',
    justifyContent: 'center',
    alignSelf: 'center',
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  redCloseButton: {
    width: 40,
    height: 40,
    backgroundColor: '#FF4040',
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    alignSelf: 'flex-end',
  },
  buttonTextLarge: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 25,
  },
  textArea: {
    width: 300,
    height: 100,
    fontSize: 16,
    borderWidth: 1,
    backgroundColor: 'white',
  },
  textAreaContainer: {
    marginTop: 100,
    alignSelf: 'center',
    justifyContent: 'center',
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  bold: {
    fontWeight: 'bold',
  },
  alignItemsCenter: {
    alignItems: 'center',
  },
});

module.exports = mainStyles;
