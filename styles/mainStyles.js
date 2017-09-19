import {StyleSheet, Dimensions} from 'react-native';

const ScreenHeight = Dimensions.get("window").height;

const mainStyles = StyleSheet.create ({
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
    width: 20,
    height: 20,
  },
  title: {
    fontWeight: 'bold',
    fontSize: 25,
    margin: 10,
  },
});

module.exports = mainStyles;
