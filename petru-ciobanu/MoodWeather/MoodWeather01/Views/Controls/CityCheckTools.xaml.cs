using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using MoodWeather.WeatherService;

namespace MoodWeather.Views.Controls
{
    public partial class CityCheckTools : ChildWindow
    {

        private WeatherService.WeatherServiceClient _weatherService;

        public CityCheckTools()
        {
            InitializeComponent();
        }

        private void OKButton_Click(object sender, RoutedEventArgs e)
        {
            responseMessage.Text = "";
            if (inputCodeCountry.Text.Length < 2 || inputCodeCity.Text.Length < 4)
            {
                inputCodeCountry.BorderBrush = new SolidColorBrush(Colors.Red);
                inputCodeCity.BorderBrush = new SolidColorBrush(Colors.Red);
            }
            else
            {
                inputCodeCountry.BorderBrush = new SolidColorBrush(Colors.Black);
                inputCodeCity.BorderBrush = new SolidColorBrush(Colors.Black);
                _weatherService = new WeatherServiceClient();
                _weatherService.CheckCityCompleted -= _weatherService_CheckCityCompleted;
                _weatherService.CheckCityCompleted += _weatherService_CheckCityCompleted;
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Fetching Service";
                _weatherService.CheckCityAsync(inputCodeCity.Text.ToUpper(), inputCodeCountry.Text.ToUpper());
            }


        }
        private void _weatherService_CheckCityCompleted(object sender, CheckCityCompletedEventArgs e)
        {
            busyIndicator.IsBusy = false;
            busyIndicator.BusyContent = "Fetching Service Complete";
            responseMessage.Text = e.Result.ToUpper();
        }
        private void CancelButton_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                _weatherService.CloseAsync();
                this.DialogResult = false;
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
            
        }
    }
}

