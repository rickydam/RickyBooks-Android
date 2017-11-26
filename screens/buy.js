import React from 'react';
import {
  Text, View, Image, ScrollView, FlatList, CheckBox,
  RefreshControl, TouchableHighlight, TouchableOpacity
} from 'react-native';
import Modal from 'react-native-modal';

const mainStyles = require('../styles/mainStyles.js');
const buyStyles = require('../styles/buyStyles.js');
const buyIcon = require('../images/icons/buy.png');
const textbook = require('../images/textbook.jpg');

class BuyScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      refreshing: false,
      data: '',
      dataOriginal: '',
      filters: [],
      refFlatList: '',
      filterModal: false,
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

  componentDidMount() {
    this.setState({
      refreshing: true
    });
    this.fetchData().then(() => {
      this.setState({
        refreshing: false
      });
    });
  }

  getDifference(postedDate, currentDate) {
    var seconds = parseInt((currentDate - postedDate)/1000);
    var minutes = parseInt(seconds/60);
    if(minutes < 60) {
      var theString;
      if(minutes < 1) {
        theString = "Just now";
      }
      else if(minutes == 1) {
        theString = minutes + " minute ago";
      }
      else {
        theString = minutes + " minutes ago";
      }
      return theString;
    }
    var hours = parseInt(minutes/60);
    if(hours < 24) {
      var theString;
      if(hours == 1) {
        theString = hours + " hour ago";
      }
      else {
        theString = hours + " hours ago";
      }
      return theString;
    }
    return null;
  }

  cleanTime(postedDate) {
    var monthNames = [
      "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
    ];
    var month = monthNames[postedDate.getMonth()];
    var day = postedDate.getDate();
    var hour = postedDate.getHours();
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
    if(postedDate.getMinutes() < 10) {
      minute = "0" + postedDate.getMinutes();
    }
    else {
      minute = postedDate.getMinutes();
    }
    var cleanDate;
    if(afternoon) {
      var cleanDate = month + " " + day + ", " + hour + ":" + minute + "pm";
    }
    else {
      var cleanDate = month + " " + day + ", " + hour + ":" + minute + "am";
    }
    return cleanDate;
  }


  async fetchData() {
    try {
      let response = await fetch('https://rickybooks.herokuapp.com/textbooks');
      let responseText = await response.text();
      this.state.data = JSON.parse(responseText).data;
      for(var item of this.state.data) {
        var postedDate = new Date(item["created_at"]);
        var cleanDate = this.cleanTime(postedDate);
        item["created_at"] = cleanDate;
        var currentDate = new Date();
        var specialTimeString = this.getDifference(postedDate, currentDate);
        if(specialTimeString != null) {
          item["created_at"] = specialTimeString;
        }
        thecoursecode = item["textbook_coursecode"].substring(0,4);
        if(this.state.filters.indexOf(thecoursecode) == -1) {
          // This is a unique entry, add it
          this.state.filters.push(thecoursecode);
        }
      }
      for(i=0; i<this.state.filters.length; i++) {
        var item = this.state.filters[i];
        this.setState({
          [item]: false
        });
      }
      this.setState({
        dataOriginal: this.state.data
      });
    } catch(error) {
      alert("error: " + error);
    }
  }

  onRefresh() {
    this.setState({
      refreshing: true
    });
    this.fetchData().then(() => {
      this.setState({
        refreshing: false,
      });
    });
  }

  showFilterModal = () => this.setState({
    filterModal: true
  });
  hideFilterModal = () => this.setState({
    filterModal: false
  });

  applyFilters() {
    this.filterData();
    this.hideFilterModal();
  }

  filterData() {
    var filterSelected = false;
    for(i=0; i<this.state.filters.length; i++) {
      var item = this.state.filters[i];
      if(this.state[item] == true) {
        filterSelected = true;
        break;
      }
    }
    if(filterSelected) {
      this.setState({
        data: this.state.dataOriginal.filter(this.filterItem.bind(this))
      });
    }
    else {
      this.setState({
        data: this.state.dataOriginal
      });
    }
    this.refFlatList.scrollToOffset({x:0, y:0, animated:true});
    this.refFlatList.scrollToOffset({x:0, y:0, animated:true}); // bug requires 2 of them
  }

  filterItem(item) {
    var theItem = item["textbook_coursecode"].substring(0,4);
    if(this.state[theItem] == true) {
      // Checkbox checked
      if(this.state.filters.indexOf(theItem) != -1) {
        // We found a match, keep it
        return true;
      }
    }
    return false;
  }

  render() {
    const {navigate} = this.props.navigation;
    return (
      <ScrollView contentContainerStyle={mainStyles.container} ref='buyScrollView'>

        <View style={buyStyles.buyTitleRow}>
          <View style={buyStyles.buyTitleView}>
            <Text style={buyStyles.buyTitleText}>Buy a textbook!</Text>
          </View>
          <TouchableOpacity
            style={buyStyles.blueButtonRight}
            onPress={this.showFilterModal}>
            <View style={buyStyles.blueButtonSmallShort}>
              <Text style={mainStyles.buttonText}>FILTER</Text>
            </View>
          </TouchableOpacity>
        </View>

        <Modal
          isVisible={this.state.filterModal}
          onBackButtonPress={this.hideFilterModal}
          backdropOpacity={0.7}>
          <View style={buyStyles.filterModalView}>

            <FlatList
              data={this.state.filters}
              extraData={this.state}
              keyExtractor={(item, index) => index}
              renderItem={({item}) =>
              <View style={mainStyles.row}>
                <View style={buyStyles.filterItemText}>
                  <Text>{item}</Text>
                </View>
                <CheckBox
                  value={this.state[item]}
                  onChange={() => {
                    if(!this.state[item]) {
                      this.setState({
                        [item]: true
                      });
                    }
                    else {
                      this.setState({
                        [item]: false,
                      });
                    }
                  }}>
                </CheckBox>
              </View>
              }
            />
          </View>
          <TouchableOpacity
            style={mainStyles.blueButtonSmall}
            onPress={this.applyFilters.bind(this)}>
            <Text style={mainStyles.buttonText}>APPLY</Text>
          </TouchableOpacity>
        </Modal>

        <FlatList
          data={this.state.data}
          extraData={this.state}
          keyExtractor={(item, index) => index}
          refreshControl={
            <RefreshControl
              refreshing={this.state.refreshing}
              onRefresh={this.onRefresh.bind(this)}/>
          }
          ref={ref => this.refFlatList=ref}
          renderItem={({item}) =>
            <View style={buyStyles.listItemMainContainer}>
              <TouchableHighlight
                style={buyStyles.listItem}
                underlayColor="#E8E8E8"
                onPress={() =>
                  navigate('TheBuyDetailsScreen', {
                    title:      item["textbook_title"],
                    author:     item["textbook_author"],
                    edition:    item["textbook_edition"],
                    condition:  item["textbook_condition"],
                    coursecode: item["textbook_coursecode"],
                    type:       item["textbook_type"],
                    created_at: item["created_at"],
                    price:      item["textbook_price"]
                  })
                }>
                <View style={mainStyles.row}>
                  <Image
                    source={textbook}
                    style={buyStyles.listItemImage}
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
                        <Text style={buyStyles.listItemDate}>{item["created_at"]}</Text>
                        <Text style={buyStyles.listItemPrice}>${item["textbook_price"]}</Text>
                      </View>
                    </View>
                  </View>
                </View>
              </TouchableHighlight>
            </View>
          }
        />
      </ScrollView>
    );
  }
}

module.exports = BuyScreen;
