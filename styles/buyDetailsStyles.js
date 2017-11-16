import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;
const ScreenHeight = Dimensions.get('window').height;

const buyDetailsStyles = StyleSheet.create ({
  itemImage: {
    height: 250,
    width: 200,
    margin: 10,
  },
  itemTitle: {
    fontSize: 25,
    fontWeight: 'bold',
    color: 'black',
    marginTop: 20,
  },
  itemText: {
    fontSize: 16,
  },
  itemPrice: {
    width: 80,
    fontSize: 30,
    fontWeight: 'bold',
    color: '#3EA055',
  },
  itemSeller: {
    width: 140,
    fontSize: 16,
  },
  itemDate: {
    width: 140,
    fontSize: 16,
    fontWeight: 'bold',
    color: 'black',
  },
  itemColumns: {
    flexDirection: 'row',

  },
  itemColumnLeft: {
    flexDirection: 'column',
    justifyContent: 'center',
    marginLeft: 60,
    width: ScreenWidth*0.35,
  },
  itemColumnRight: {
    flexDirection: 'column',
    justifyContent: 'center',
    paddingLeft: 20,
    width: ScreenWidth*0.5,
  },
});

module.exports = buyDetailsStyles;
