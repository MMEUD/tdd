using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using MoodWeather.WeatherService;
using SelectionChangedEventArgs = Telerik.Windows.Controls.SelectionChangedEventArgs;

namespace MoodWeather.Views.Controls
{
    public partial class WeatherInfo : UserControl
    {
        private WeatherServiceClient _client;
        private WeatherIcons _weatherData;


        public WeatherInfo()
        {
            InitializeComponent();
            LoadCityList();
        }

        //LOAD CITY LISTS 
        public void LoadCityList()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.SelectCityCompleted -= client_SelectCityCompleted;
                _client.SelectCityCompleted += client_SelectCityCompleted;
                _client.SelectCityAsync();
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
        }

        private void client_SelectCityCompleted(object sender, SelectCityCompletedEventArgs e)
        {
            cityRequest.Content = e.Result.Count;
            CountCity();
            for (int i = 0; i < e.Result.Count; i++)
            {
                cityList.Items.Add(e.Result[i].CityName);
            }
        }

        public void CountCity()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.GetCityConditionCompleted -= client_GetCityConditionCompleted;
                _client.GetCityConditionCompleted += client_GetCityConditionCompleted;

                _client.GetCityConditionAsync();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_GetCityConditionCompleted(object sender, GetCityConditionCompletedEventArgs e)
        {
            try
            {
                cityUpdate.Content = e.Result.Count/4;
                _client.CloseAsync();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        public void LoadConditionDb(string cityName)
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.SelectCityConditionCompleted -= client_GetCityConditionCompleted;
                _client.SelectCityConditionCompleted += client_GetCityConditionCompleted;
                _client.SelectCityConditionAsync(cityName);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_GetCityConditionCompleted(object sender, SelectCityConditionCompletedEventArgs e)
        {
            try
            {
                cityTimeUpdate.Content = e.Result[0].LastUpdate;
                cityUpdateBy.Content = e.Result[0].UpdateBy;
                for (int i = 0; i < e.Result.Count; i++)
                {
                    _weatherData = new WeatherIcons();
                    _weatherData.lblCity.Content = e.Result[i].CityName;
                    _weatherData.lblDate.Content = e.Result[i].Date;
                    _weatherData.lblCondition.Content = e.Result[i].Condition;
                    _weatherData.lblWeekDay.Content = e.Result[i].WeekDay;
                    _weatherData.lblSunrise.Content = e.Result[i].Sunrise + "AM";
                    _weatherData.lblSunset.Content = e.Result[i].Sunset + "PM";
                    _weatherData.lblTempHigh.Content = e.Result[i].TempHigh;
                    _weatherData.lblTempLow.Content = e.Result[i].TempLow;
                    _weatherData.lblCountry.Content = e.Result[i].CountryCode;
                    _weatherData.lblImage.Source =
                        new BitmapImage(new Uri("/MoodWeather;component/Assets/Icons/" + e.Result[i].Icon + ".png",
                                                UriKind.Relative));
                    _weatherData.Margin = new Thickness((275*i), 5, 0, 0);
                    container.Children.Add(_weatherData);
                }
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
            _client.CloseAsync();
        }

        private void cityList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            try
            {
                container.Children.Clear();
                LoadConditionDb(cityList.SelectedValue.ToString());
            }
            catch (Exception)
            {
            }
        }

        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
        }
    }
}