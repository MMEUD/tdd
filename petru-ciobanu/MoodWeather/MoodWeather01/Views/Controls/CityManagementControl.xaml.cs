using System;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;
using MoodWeather.CityListService;
using MoodWeather.WeatherService;

namespace MoodWeather.Views.Controls
{
    public partial class CityManagementControl : UserControl
    {
        private PagedCollectionView _view;
        private bool _insertOperation;
        CityListService.CityServiceClient _client;
      
        public CityManagementControl()
        {
            InitializeComponent();
            contFrm.Visibility = System.Windows.Visibility.Collapsed;
        }

        //LOAD CITY SERVICE OPERATION
        private void GetCityList()
        {
            try
            {
                _client = new CityServiceClient();
                _client.OpenAsync();
                _client.GetCityListCompleted -= client_GetCityListCompleted;
                _client.GetCityListCompleted += client_GetCityListCompleted;
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Fetching Service";
                _client.GetCityListAsync();

            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
        }

        private void client_GetCityListCompleted(object sender, GetCityListCompletedEventArgs e)
        {
            if (e.Result.Count == 0)
            {
                _insertOperation = true;
                contFrm.Visibility = System.Windows.Visibility.Visible;
            }
            RadDataPager1.Width = 1245;
            CityList.Width = 1245;
            busyIndicator.BusyContent = "Fetching Service Complete";
            busyIndicator.IsBusy = false;
            _view = new PagedCollectionView(e.Result);
            RadDataPager1.Source = _view;
            RadDataPager1.ItemCount = _view.TotalItemCount;
            cityCounts.Content = _view.TotalItemCount;
            CityList.ItemsSource = _view;
           _client.CloseAsync();
        }

        //INSERT CITY SERVICE OPERATION
        private void InsertCity(string city, string cityalternative, string country, string countrycode,string countrycodeteamco,string active)
        {
            try
            {
                _client = new CityServiceClient();
                _client.OpenAsync();
                _client.InsertCityCompleted -= client_InsertCityCompleted;
                _client.InsertCityCompleted += client_InsertCityCompleted;
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Insert Data Operation";
                _client.InsertCityAsync(city,cityalternative, country, countrycode,countrycodeteamco, WebContext.Current.User.FriendlyName,active);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_InsertCityCompleted(object sender, AsyncCompletedEventArgs e)
        {
            try
            {
                RadDataPager1.Width = 1245;
                CityList.Width = 1245;
                busyIndicator.BusyContent = "InsertDataCompleted";
                busyIndicator.IsBusy = false;
                _client.CloseAsync();
                GetCityList();
                contFrm.Visibility = Visibility.Collapsed;
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        //UPDATE SERVICE  OPERATION 
        private void UpdateCity(string id, string city,string cityalternative, string country, string countrycode,string countrycodeteamco,string active)
        {
            try
            {
                _client = new CityServiceClient();
                _client.OpenAsync();
                _client.UpdateCityCompleted -= client_UpdateCityCompleted;
                _client.UpdateCityCompleted += client_UpdateCityCompleted;
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Update Data Operation";
                _client.UpdateCityAsync(Convert.ToInt32(id), city,cityalternative, country, countrycode,countrycodeteamco, WebContext.Current.User.FriendlyName.ToString(),active);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_UpdateCityCompleted(object sender, AsyncCompletedEventArgs e)
        {
            try
            {
                busyIndicator.BusyContent = "UpdateDataCompleted";
                busyIndicator.IsBusy = false;
                _client.CloseAsync();
                GetCityList();
                contFrm.Visibility = Visibility.Collapsed;
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        //DELETE SERVICE OPERATION
        private void DeleteCity(int id)
        {
            try
            {
                _client = new CityServiceClient();
                _client.OpenAsync();
                _client.DeleteCityCompleted += client_DeleteCityCompleted;
                _client.DeleteCityCompleted += client_DeleteCityCompleted;
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Delete Data Operation";
                _client.DeleteCityAsync(id);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_DeleteCityCompleted(object sender, AsyncCompletedEventArgs e)
        {
            try
            {
                busyIndicator.BusyContent = "DeleteDataCompleted";
                busyIndicator.IsBusy = false;
                _client.CloseAsync();
                GetCityList();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        //DATA GRID BUTTONS COMMAND
        private void btnUpdate_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                RadDataPager1.Width = 810;
                CityList.Width = 810;
                var selRow = CityList.CurrentItem as CityLists;
                inputCity.Text = selRow.CityName;
                inputCountry.Text = selRow.Country;
                inputCountryCode.Text = selRow.CountryCode;
                inputCityAlternative.Text = selRow.CityAlternative;
                lblId.Content = selRow.Id;
                insert_update("update", selRow.Id.ToString(), selRow.CityName, selRow.CityAlternative, selRow.Country, selRow.CountryCode,selRow.CountryCodeTeamco,selRow.Active);
                contFrm.Visibility = Visibility.Visible;
                lblMsg.Content = "Update Data";
                _insertOperation = false;
                inputCountryCode.MouseEnter -= new MouseEventHandler(inputCountryCode_MouseEnter);
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void btnInsert_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                RadDataPager1.Width = 810;
                CityList.Width = 810;
                _insertOperation = true;
                lblMsg.Content = "Insert New Data";
                inputCountry.Text = "";
                inputCity.Text = "";
                inputCountryCode.Text = "";
                inputCountryCodeTeamco.Text = "";
                contFrm.Visibility = Visibility.Visible;
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void btnDelete_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                MessageBoxResult result = MessageBox.Show("Do you want to delete this record ?", "Data Delete Confirmation", MessageBoxButton.OKCancel);

                if (result == MessageBoxResult.OK)
                {
                    var selRow = CityList.CurrentItem as CityLists;
                    if (selRow != null) DeleteCity(Convert.ToInt32(selRow.Id));
                }
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void insert_update(string type, string id, string city,string cityalternative, string country, string countryCode,string countryCodeTeamco,string cityActive)
        {
            if (type == "update")
            {
                inputCity.Text = city;
                inputCountry.Text = country;
                inputCountryCode.Text = countryCode;
                inputCountryCodeTeamco.Text = countryCodeTeamco;
                inputCityAlternative.Text = cityalternative;
                
                if (cityActive == "1")
                {
                    inputActiveCity.IsChecked = true;
                }
                else
                {
                    inputActiveCity.IsChecked = false; 
                }
                
                
                lblId.Content = id;
            }
            else
            {
                _insertOperation = true;
            }
        }

        //SEND OPERATION TO SERVICE
        private void btnSend_Click(object sender, RoutedEventArgs e)
        {
            if (inputCity.Text.Length < 3  || inputCityAlternative.Text.Length <3 || inputCountry.Text.Length < 4 || inputCountryCode.Text.Length > 3 || inputCountryCode.Text.Length < 2||inputCountryCodeTeamco.Text.Length<2)
            {
                lblMsg.Background = new SolidColorBrush(Colors.Red);
                lblMsg.Foreground = new SolidColorBrush(Colors.White);
                lblMsg.Content = "Please Complete All Fields";
                inputCity.BorderBrush = new SolidColorBrush(Colors.Red);
                inputCityAlternative.BorderBrush=new SolidColorBrush(Colors.Red);
                inputCountryCode.MouseEnter -= new MouseEventHandler(inputCountryCode_MouseEnter);
                inputCountryCode.MouseEnter += new MouseEventHandler(inputCountryCode_MouseEnter);
                inputCountry.BorderBrush = new SolidColorBrush(Colors.Red);
                inputCountryCode.BorderBrush = new SolidColorBrush(Colors.Red);
              
            }
            else
            {
                if (_insertOperation == true)
                {

                    lblMsg.Background = new SolidColorBrush(Colors.White);
                    lblMsg.Foreground = new SolidColorBrush(Colors.Black);
                    inputCity.BorderBrush = new SolidColorBrush(Colors.Black);
                    inputCityAlternative.BorderBrush=new SolidColorBrush(Colors.Black);
                    inputCountry.BorderBrush = new SolidColorBrush(Colors.Black);
                    inputCountryCode.BorderBrush = new SolidColorBrush(Colors.Black);
                    inputCountryCode.Width = 55;
                    lblMsg.Content = "Insert New Data";
                    InsertCity(inputCity.Text, inputCityAlternative.Text, inputCountry.Text, inputCountryCode.Text,inputCountryCodeTeamco.Text,"1");


                }
                else
                {
                    lblMsg.Background = new SolidColorBrush(Colors.White);
                    lblMsg.Foreground = new SolidColorBrush(Colors.Black);
                    inputCity.BorderBrush = new SolidColorBrush(Colors.Black);
                    inputCountry.BorderBrush = new SolidColorBrush(Colors.Black);
                    inputCountryCode.BorderBrush = new SolidColorBrush(Colors.Black);
                    inputCountryCode.Width = 55;
                    lblMsg.Content = "Update Data";

                    if (inputActiveCity.IsChecked == true)
                    {
                        UpdateCity(lblId.Content.ToString(), inputCity.Text, inputCityAlternative.Text, inputCountry.Text, inputCountryCode.Text, inputCountryCodeTeamco.Text, "1");
                    }
                    else
                    {
                        UpdateCity(lblId.Content.ToString(), inputCity.Text, inputCityAlternative.Text,inputCountry.Text, inputCountryCode.Text, inputCountryCodeTeamco.Text, "0");
                    }


                    
                }
            }

        }

        private void btnCancel_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                RadDataPager1.Width = 1245;
                CityList.Width = 1245;
                contFrm.Visibility = Visibility.Collapsed;
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void inputCountryCode_MouseEnter(object sender, MouseEventArgs e)
        {
            try
            {
                inputCountryCode.Text = "";
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void BtnLoadCity_Click_1(object sender, RoutedEventArgs e)
        {
            try
            {
                GetCityList();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void btnCheckCityStatus_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                var cityCheckTools = new CityCheckTools();
                cityCheckTools.Show();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

       
    }
}
