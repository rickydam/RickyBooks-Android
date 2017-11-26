import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;
const ScreenHeight = Dimensions.get('window').height;

const buyStyles = StyleSheet.create ({
  buyTitleText: {
    width: ScreenWidth-80,
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 25,
    margin: 5,
    alignSelf: 'center',
  },
  buyTitleView: {
    width: ScreenWidth-80,
    justifyContent: 'center',
  },
  listItemMainContainer: {
    width: ScreenWidth,
    height: 150,
    borderTopWidth: 1,
  },
  buyTitleRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  blueButtonSmallShort: {
    width: 60,
    height: 30,
    margin: 10,
    borderRadius: 5,
    backgroundColor: '#0276FD',
    alignItems: 'center',
    justifyContent: 'center',
  },
  blueButtonRight: {
    width: 80,
  },
  filterModalView: {
    width: 120,
    height: 300,
    padding: 10,
    backgroundColor: 'white',
    alignSelf: 'center',
    alignItems: 'center',
  },
  filterItemText: {
    width: 45,
  },
  listItem: {
    width: ScreenWidth,
    height: 150,
    paddingLeft: 5,
    flex: 1,
    flexDirection: 'row',
  },
  listItemImage: {
    width: ScreenWidth*0.27,
    height: 130,
  },
  listItemTextContainer: {
    width: ScreenWidth*0.70,
    height: 125,
    paddingLeft: 10,
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
  listItemCondition: {
    fontSize: 14,
  },
  listItemType: {
    fontSize: 14,
  },
  listItemPrice: {
    fontSize: 25,
    fontWeight: 'bold',
    color: '#3EA055'
  },
  listItemSeller: {
    fontSize: 14,
  },
  listItemDate: {
    fontSize: 14,
    fontWeight: 'bold',
    color: 'black',
  },
  listItemColumns: {
    flexDirection: 'row',
    borderTopWidth: 1,
    borderTopColor: '#E0E0E0',
    marginTop: 2,
  },
  listItemLeft: {
    width: 120,
    marginTop: 2,
  },
  listItemRight: {
    width: 120,
    marginTop: 2,
  },
});

module.exports = buyStyles;
