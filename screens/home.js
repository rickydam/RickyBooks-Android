import React from 'react';
import {View, Button} from 'react-native';
const styles = require('../styles.js');

class HomeScreen extends React.Component {
  static navigationOptions = {
    title: 'Home'
  };
  render() {
    const {navigate} = this.props.navigation;
    return (
      <View style={styles.container}>
        <Button
          onPress = {() => navigate('theTextbooks')}
          title = 'Textbooks'
          color = '#9932CD'
        />
      </View>
    );
  }
}
module.exports = HomeScreen;
