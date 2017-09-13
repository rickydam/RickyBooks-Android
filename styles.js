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
});

module.exports = styles;
