import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get("window").width;
const ScreenHeight = Dimensions.get("window").height;

const buyStyles = StyleSheet.create ({
  listItem: {
    width: ScreenWidth,
    height: ScreenHeight*0.25,
    borderTopWidth: 1,
    paddingLeft: 5,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  listItemImage: {
    width: ScreenWidth*0.25,
    height: ScreenHeight*0.23,
  },
  listItemTextContainer: {
    width: ScreenWidth*0.70,
    height: ScreenHeight*0.20,
    paddingLeft: 5,
  },
  listItemCourseCode: {
    fontSize: 14,
  },
  listItemTitle: {
    fontWeight: 'bold',
    fontSize: 14,
    color: 'black',
  },
  listItemAuthor: {
    fontSize: 14,
  },
  listItemEdition: {
    fontSize: 14,
  },
  listItemPrice: {
    fontSize: 25,
    fontWeight: 'bold',
    color: '#3EA055'
  },
  listItemSeller: {
    fontSize: 14,
    textAlign: 'center',
  },
  listItemDate: {
    fontSize: 14,
    fontWeight: 'bold',
    color: 'black',
    textAlign: 'center',
  },
  listItemColumns: {
    flexDirection: 'row',
  },
  listItemLeft: {
    width: ScreenWidth*0.2,
    flexDirection: 'column',
  },
  listItemRight: {
    flexDirection: 'column',
  },
});

module.exports = buyStyles;
