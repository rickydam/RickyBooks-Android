import {StyleSheet, Dimensions} from 'react-native';

const profileStyles = StyleSheet.create ({
  profileImage: {
    width: 150,
    height: 150,
    margin: 5,
  },
  name: {
    fontSize: 25,
    fontWeight: 'bold',
    color: 'black',
    marginBottom: 10,
  },
  normalText: {
    color: 'black',
  },
  signOutContainer: {
    alignSelf: 'flex-end',
    marginLeft: 'auto',
    padding: 5,
  },
});

module.exports = profileStyles;
