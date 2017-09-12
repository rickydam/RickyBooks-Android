import React from 'react';
import {Text, View} from 'react-native';
const styles = require('../styles.js');

class TextbookScreen extends React.Component {
  static navigationOptions = {
    title: 'Textbooks'
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={styles.container}>
        <Text>This is the textbooks screen.</Text>
      </View>
    );
  }
}
module.exports = TextbookScreen;
