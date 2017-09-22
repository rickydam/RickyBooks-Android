import {StyleSheet, Dimensions} from 'react-native';

const ScreenHeight = Dimensions.get("window").height;

const mainStyles = StyleSheet.create ({
  container: {
    backgroundColor: '#fff',
    alignItems: 'center',
  },
  tabContainer: {
    height: ScreenHeight-20,
    backgroundColor: 'white',
  },
  icon: {
    width: 20,
    height: 20,
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
