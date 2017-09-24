import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;

const messagesStyles = StyleSheet.create ({
  messagesContainer: {
    width: ScreenWidth,
    height: 80,
    borderTopWidth: 1,
  },
  columns: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    padding: 5,
  },
  leftColumn: {
    width: 50,
    height: 50,
    backgroundColor: 'white',
    margin: 5,
  },
  rightColumn: {
    width: ScreenWidth*0.77,
    alignSelf: 'flex-start',
    margin: 5,
  },
  userImage: {
    width: 50,
    height: 50,
  },
  listName: {
    fontSize: 15,
    fontWeight: 'bold',
    color: 'black',
  },
  listPreview: {
    fontSize: 13,
    marginTop: 5,
  },
  nameDate: {
    flexDirection: 'row',
  },
  listDate: {
    fontSize: 15,
    fontWeight: 'bold',
    color: 'black',
    marginLeft: 'auto',
  },
});

module.exports = messagesStyles;
