import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;
const ScreenHeight = Dimensions.get('window').height;

const buyStyles = StyleSheet.create ({
  listItemMainContainer: {
    width: ScreenWidth,
    height: ScreenHeight*0.23,
    borderTopWidth: 1,
  },
  listItem: {
    width: ScreenWidth,
    height: ScreenHeight*0.23,
    paddingLeft: 5,
    flex: 1,
    flexDirection: 'row',
    marginTop: 8,
  },
  listItemImage: {
    width: ScreenWidth*0.27,
    height: ScreenHeight*0.20,
  },
  listItemTextContainer: {
    width: ScreenWidth*0.70,
    height: ScreenHeight*0.20,
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
  itemImage: {
    height: 250,
    width: 200,
    margin: 20,
  },
  itemTitle: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  itemAuthor: {
    fontSize: 16,
  },
  itemEdition: {
    fontSize: 16,
  },
  itemPrice: {
    fontSize: 30,
    fontWeight: 'bold',
    color: '#3EA055',
    margin: 10,
  },
  itemSeller: {
    fontSize: 16,
  },
  itemDate: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  itemColumns: {
    flexDirection: 'row',
  },
  itemColumn: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    width: ScreenWidth*0.3,
  },
});

module.exports = buyStyles;
