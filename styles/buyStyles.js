import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get("window").width;
const ScreenHeight = Dimensions.get("window").height;

const buyStyles = StyleSheet.create ({
  listItem: {
    width: ScreenWidth,
    height: ScreenHeight*0.30,
    borderTopWidth: 1,
    paddingLeft: 5,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  listItemImage: {
    width: ScreenWidth*0.40,
    height: ScreenHeight*0.28,
    marginTop: 5,
  },
  listItemTextContainer: {
    width: ScreenWidth*0.60,
    paddingLeft: 5,
  },
  listItemTitle: {
    fontWeight: 'bold',
    fontSize: 15,
    color: 'black',
  },
  listItemAuthor: {
    fontSize: 15,
  },
  listItemEdition: {
    fontSize: 15,
  },
  listItemPrice: {
    fontSize: 25,
    fontWeight: 'bold',
    color: '#3EA055'
  },
  listItemDate: {
    fontSize: 15,
    fontWeight: 'bold',
    color: 'black',
  },
});

module.exports = buyStyles;
