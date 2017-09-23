import {StyleSheet, Dimensions} from 'react-native';

const ScreenHeight = Dimensions.get("window").height;

const mainStyles = StyleSheet.create ({
  container: {
    backgroundColor: 'white',
    alignItems: 'center',
    paddingLeft: 5,
    paddingRight: 5,
  },
  tabContainer: {
    height: ScreenHeight-20,
    backgroundColor: 'white',
  },
  detailsContainer: {
    backgroundColor: 'white',
    height: ScreenHeight,
    alignItems: 'center',
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
    fontWeight: 'bold',
    fontSize: 25,
    margin: 5,
  },
  blueButton: {
    width: 200,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  blueButtonSmall: {
    width: 100,
    height: 40,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
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
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
  },
});

module.exports = mainStyles;
