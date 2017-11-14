import React from 'react';
import {Text, View, Image, ScrollView, FlatList, RefreshControl, TouchableOpacity} from 'react-native';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

class BuyScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      refreshing: true,
      res: '',
    };
  }

  static navigationOptions = {
    tabBarLabel: 'Buy',
    tabBarIcon: ({tintColor}) => (
      <Image
        source = {buyIcon}
        style = {[mainStyles.icon, {tintColor: tintColor}]}
      />
    ),
  };

  async componentWillMount() {
    this.setState({refreshing:true});
    this.fetchData().then(() => {
      this.setState({refreshing: false});
    });
  }

  async fetchData() {
    try {
      let response = await fetch('https://rickybooks.herokuapp.com/textbooks');
      let responseText = await response.text();
      this.state.res = JSON.parse(responseText).data;

      var monthNames = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
      ];

      for(var item of this.state.res) {
        var postedDate = item["created_at"];
        var formattedDate = new Date(postedDate);

        var month = monthNames[formattedDate.getMonth()];
        var day = formattedDate.getDate();
        var year = formattedDate.getFullYear();

        var hour = formattedDate.getHours();
        var afternoon = false;
        if(hour == 12) {
          afternoon = true;
        }
        if(hour > 12) {
          afternoon = true;
          hour = hour - 12;
        }
        if(hour == 0) {
          hour = 12;
        }
        var minute;
        if(formattedDate.getMinutes() < 10) {
          minute = "0" + formattedDate.getMinutes();
        }
        else {
          minute = formattedDate.getMinutes();
        }

        var cleanDate;
        if(afternoon) {
          var date = month + " " + day + ", " + year;
          var time = hour + ":" + minute + "pm";
          cleanDate = [date, time];
        }
        else {
          var date = month + " " + day + ", " + year;
          var time = hour + ":" + minute + "am";
          cleanDate = [date, time];
        }
        item["created_at"] = cleanDate;
      }
    } catch(error) {
      alert("error: " + error);
    }
  }

  _onRefresh() {
    this.setState({refreshing:true});
    this.fetchData().then(() => {
      this.setState({refreshing: false});
    });
  }

  render() {
    const {navigate} = this.props.navigation;
    return (
      <ScrollView contentContainerStyle={mainStyles.container}>
        <Text style={mainStyles.title}>Buy a textbook!</Text>
        <FlatList
          data={this.state.res}
          extraData={this.state}
          keyExtractor={(item, index) => index}
          refreshControl={
            <RefreshControl
              refreshing={this.state.refreshing}
              onRefresh={this._onRefresh.bind(this)}/>
          }
          renderItem={({item}) =>
            <View style={buyStyles.listItemMainContainer}>
              <TouchableOpacity
                style={buyStyles.listItem}
                onPress={() => navigate('TheBuyDetailsScreen')}
                activeOpacity={100}>
                <Image
                  source = {textbook}
                  style = {buyStyles.listItemImage}
                />
                <View style={buyStyles.listItemTextContainer}>
                  <Text style={buyStyles.listItemTitle}>{item["textbook_title"]}</Text>
                  <Text style={buyStyles.listItemAuthor}>{item["textbook_author"]}</Text>
                  <View style={buyStyles.listItemColumns}>
                    <View style={buyStyles.listItemLeft}>
                      <Text style={buyStyles.listItemEdition}>{item["textbook_edition"]}</Text>
                      <Text style={buyStyles.listItemCondition}>{item["textbook_condition"]}</Text>
                      <Text style={buyStyles.listItemCourseCode}>{item["textbook_coursecode"]}</Text>
                      <Text style={buyStyles.listItemType}>{item["textbook_type"]}</Text>
                    </View>
                    <View style={buyStyles.listItemRight}>
                      <Text style={buyStyles.listItemSeller}>Ricky Dam</Text>
                      <Text style={buyStyles.listItemDate}>{item["created_at"][0]}</Text>
                      <Text style={buyStyles.listItemTime}>{item["created_at"][1]}</Text>
                      <Text style={buyStyles.listItemPrice}>${item["textbook_price"]}</Text>
                    </View>
                  </View>
                </View>
              </TouchableOpacity>
            </View>
          }
        />
      </ScrollView>
    );
  }
}

module.exports = BuyScreen;
