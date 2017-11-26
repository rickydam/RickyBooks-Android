import {StyleSheet, Dimensions} from 'react-native';

const ScreenWidth = Dimensions.get('window').width;
const ScreenHeight = Dimensions.get('window').height;

const buyDetailsStyles = StyleSheet.create ({
  detailsContainer: {
    backgroundColor: 'white',
    height: ScreenHeight,
    alignItems: 'center',
  },
  messageModalContainer: {
    justifyContent: 'flex-start',
  },
  imageModalView: {
    width: ScreenWidth,
    height: ScreenHeight,
    justifyContent: 'center',
  },
  modalImage: {
    width: ScreenWidth*0.90,
    height: ScreenHeight*0.70,
  },
  itemImage: {
    height: 250,
    width: 200,
    margin: 10,
  },
  itemTitle: {
    width: ScreenWidth,
    textAlign: 'center',
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
  messageButtonText: {
    color: 'white',
    fontWeight: 'bold',
    width: 100,
    textAlign: 'center',
  },
  sendButtonText: {
    color: 'white',
    fontWeight: 'bold',
    width: 60,
    textAlign: 'center',
  },
});

module.exports = buyDetailsStyles;
