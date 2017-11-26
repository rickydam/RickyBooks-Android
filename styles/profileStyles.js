import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;

const profileStyles = StyleSheet.create ({
  profileImage: {
    width: 150,
    height: 150,
    margin: 5,
  },
  name: {
    width: ScreenWidth,
    textAlign: 'center',
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
  editButtonText: {
    color: 'white',
    fontWeight: 'bold',
    width: 100,
    textAlign: 'center',
  },
});

module.exports = profileStyles;
