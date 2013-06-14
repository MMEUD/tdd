using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Threading;
using MoodWeather.UploadService;
using MoodWeather.WeatherService;

namespace MoodWeather.Views.Controls
{
    public partial class WeatherCondition : UserControl
    {
        private WeatherService.WeatherServiceClient _client = null;

        private int _countCity;
        private string _fileName;
        private int _idx;
        private ObservableCollection<ListaOrase> _listOrase;
        private DispatcherTimer _timerCity = new DispatcherTimer();
        private PagedCollectionView _view;
        private byte[] _bytes;
       
        public WeatherCondition()
        {
            InitializeComponent();
        }

        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
            if (!System.ComponentModel.DesignerProperties.GetIsInDesignMode(this))
            {
                var myCollectionViewSource =
                    (System.Windows.Data.CollectionViewSource)this.Resources["Resource Key for CollectionViewSource"];
            }
        }

        // 1 START SERVICE BY THE USER
        public void StartService()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.StartDataCollectCompleted -= client_StartDataCollectCompleted;
                _client.StartDataCollectCompleted += client_StartDataCollectCompleted;
                busyIndicator.BusyContent = "Send Command To Service";
                busyIndicator.IsBusy = true;
                _client.StartDataCollectAsync("userCommand");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_StartDataCollectCompleted(object sender, AsyncCompletedEventArgs e)
        {
            
            if (e.Error != null)
            {
                busyIndicator.BusyContent = "Send Command To Service";
                busyIndicator.IsBusy = false;
                MessageBox.Show("ErrorFetchingService" + e.Error.Message);
                _client.CloseAsync();
            }
            else
            {
                _client.CloseAsync();
                busyIndicator.BusyContent = "Send Command To Service";
                busyIndicator.IsBusy = false;
                DeleteDatabase();
            }     
           
        }

        // 2 DELETE DATABASE
        public void DeleteDatabase()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.DeleteDatabaseCompleted -= client_DeleteDatabaseCompleted;
                _client.DeleteDatabaseCompleted += client_DeleteDatabaseCompleted;
                _client.DeleteDatabaseAsync();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }


        private void client_DeleteDatabaseCompleted(object sender, AsyncCompletedEventArgs e)
        {
            try
            {
                _client.CloseAsync();
                busyIndicator.BusyContent = "Send Command To Service";
                busyIndicator.IsBusy = true;
                LoadCityList();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }


        // 3 LOAD CITY LISTS
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
                Console.WriteLine(e);
            }
        }

        private void client_SelectCityCompleted(object sender, SelectCityCompletedEventArgs e)
        {
            _listOrase = new ObservableCollection<ListaOrase>();
            for (int i = 0; i < e.Result.Count; i++)
            {
                _listOrase.Add(new ListaOrase { CityName = e.Result[i].CityName, CountryCode = e.Result[i].CountryCode, CountryCodeTeamco = e.Result[i].CountryCodeTeamco ,CityAlternative = e.Result[i].CityAlternative,CityId = e.Result[i].CityId});
                if (_listOrase.Count == e.Result.Count)
                {
                    _countCity = _listOrase.Count;
                     EvaluateCity();
                    _client.CloseAsync();
                   
                     busyIndicator.IsBusy = true;
                     busyIndicator.BusyContent = "Please Wait :" + ((_countCity * 30)) + " seconds";
                }
            }
        }



        //4 EVALUATE CITY  LIST
        public void EvaluateCity()
        {
            if (_idx < _countCity)
            {
                NextPosition(_idx);
               
            }
            else
            {
                if (_idx == _countCity)
                {
                    busyIndicator.IsBusy = false;
                    busyIndicator.BusyContent = "Receive Data From Weather.com Completed";
                    MessageBox.Show("Receive Data From Weather.com Completed");
                    LoadConditionDb();
                }
            }
        }



        //5 SEND REQUEST TO WEATHER.COM
        public void NextPosition(int nr)
        {
            try
            {
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Please Wait :" + (((_countCity-nr) * 30)) + " seconds";
                SendWeather(_listOrase.ElementAt(nr).CountryCode, _listOrase.ElementAt(nr).CountryCodeTeamco, _listOrase.ElementAt(nr).CityName, _listOrase.ElementAt(nr).CityAlternative ,  "userCommands",_listOrase.ElementAt(nr).CityId);
                TimerCitys();
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
        }

        // 6 INSERT RESULT FROM  WEATHER.COM INTO DATABASE
        public void SendWeather(string countrycode, string countrycodeteamco, string cityname, string cityalternative,string user,string cityId)
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.GetWeatherForecastCompleted -= client_GetWeatherForecastCompleted;
                _client.GetWeatherForecastCompleted += client_GetWeatherForecastCompleted;
                _client.GetWeatherForecastAsync(countrycode, countrycodeteamco, cityname, cityalternative, user,cityId);
      
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void client_GetWeatherForecastCompleted(object sender, AsyncCompletedEventArgs e)
        {

           _client.CloseAsync();
        }


        //LOAD WEATHER FORECAST  DATA FROM DATABASE
        public void LoadConditionDb()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.GetCityConditionCompleted -=client_GetCityConditionCompleted;
                _client.GetCityConditionCompleted += client_GetCityConditionCompleted;
                busyIndicator.BusyContent = "Load Weather Condition";
                busyIndicator.IsBusy = true;
                _client.GetCityConditionAsync();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }


        private void client_GetCityConditionCompleted(object sender, GetCityConditionCompletedEventArgs e)
        {
            if (e.Error != null)
            {
                busyIndicator.BusyContent = "Load Weather Complete Error!";
                busyIndicator.IsBusy = false;
                MessageBox.Show("Weather Condition Error !", " Weather Condition Operation ", MessageBoxButton.OK);
            }
            else
            {
                busyIndicator.IsBusy = false;
                busyIndicator.BusyContent = "Load City  Condition Completed !";
                _view = new PagedCollectionView(e.Result);
                GridView.ItemsSource = _view;
                dataPager.Source = _view;
                dataPager.ItemCount = _view.TotalItemCount;
                cityCounts.Content = e.Result.Count / 4;
                _client.CloseAsync();
            }

        }

        //TIMERS FOR TRIGGER WEATHER.COM
        public void TimerCitys()
        {
            try
            {
                _timerCity = new DispatcherTimer();
                _timerCity.Interval = new TimeSpan(0, 0, 30); // 30 seconds
                _timerCity.Tick += new EventHandler(timerCity_Tick);
                _timerCity.Start();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public void timerCity_Tick(object sender, EventArgs ev)
        {
            try
            {
                _idx++;
                EvaluateCity();

            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

     
        

        //GENERATE XML  FILE  TO  SERVER 
        private void BtnGenerateXml_Click_1(object sender, RoutedEventArgs e)
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.SaveXmlFileCompleted -= client_generateXmlFileCompleted;
                _client.SaveXmlFileCompleted += client_generateXmlFileCompleted;
                busyIndicator.BusyContent = "Generate Xml File";
                busyIndicator.IsBusy = true;
                _client.SaveXmlFileAsync();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void client_generateXmlFileCompleted(object sender, AsyncCompletedEventArgs e)
        {
            if (e.Error != null)
            {
                busyIndicator.BusyContent = "Generate Xml Error!";
                busyIndicator.IsBusy = false;
                MessageBox.Show("Generate Xml File Error !", "Generate Xml Operation", MessageBoxButton.OK);
                _client.CloseAsync();
            }
            else
            {
                busyIndicator.BusyContent = "Generate Xml FileComplete";
                busyIndicator.IsBusy = false;
                MessageBox.Show("Generate Xml FileComplete", "Generate Xml Operation", MessageBoxButton.OK);
                _client.CloseAsync();  
            }
          
        }


        //DOWNLOAD  XML  FILE  FROM SERVER FOR MODIFY  ALERT TAGS
        private void BtnDownloadXml_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                var host = Application.Current.Host.Source.AbsoluteUri;
                host = host.Remove(host.IndexOf("/ClientBin", StringComparison.Ordinal));
                _fileName = host.Remove(host.IndexOf("weather", StringComparison.Ordinal)) + "wcf" + "/App_Data/" + "weather_forecast.xml";
                var dialog = new SaveFileDialog { Filter = "xml Files|*.xml", DefaultFileName = "weather_forecast.xml" };
                bool? dialogResult = dialog.ShowDialog();
                if (dialogResult != true) return;
                var webClient = new WebClient();
                webClient.OpenReadCompleted += (s, es) =>
                    {
                        try
                        {
                            using (var fs = (Stream)dialog.OpenFile())
                            {
                                es.Result.CopyTo(fs);
                                fs.Flush();
                                fs.Close();
                            }
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Server Error !"+ex.Message, "Download Xml File Operation ", MessageBoxButton.OK);
                        }
                    };
                webClient.OpenReadAsync(new Uri(_fileName), UriKind.Absolute);
            }

            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        // LOAD WEATHER CONDITION FROM DB 
        private void BtnGetCondition_Click_1(object sender, RoutedEventArgs e)
        {
            try
            {
                LoadConditionDb();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }
        //START SERVICE MANUALLY
        private void BtnStartService_Click_1(object sender, RoutedEventArgs e)
        {
            try
            {
                StartService();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        //UPLAOD  XML  FILE  MODIFIED BY THE USER 
        private void BtnUploadXml_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                var dialog = new OpenFileDialog();
                dialog.Filter = "xml Files|*.xml";
                bool? dialogResult = dialog.ShowDialog();
                if (dialogResult != true) return;
                using (Stream stream = dialog.File.OpenRead())
                {
                    var streams = (Stream)dialog.File.OpenRead();
                    _bytes = new byte[streams.Length];
                    streams.Read(_bytes, 0, (int)streams.Length);
                }

                var uploadServiceClient = new UploadServiceClient();
                uploadServiceClient.FileUploadCompleted -= uploadServiceClient_FileUploadCompleted;
                uploadServiceClient.FileUploadCompleted += uploadServiceClient_FileUploadCompleted;
                busyIndicator.BusyContent = "Upload Xml File To Server";
                busyIndicator.IsBusy = true;
                uploadServiceClient.FileUploadAsync(_bytes);
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }

        private void uploadServiceClient_FileUploadCompleted(object sender, UploadService.FileUploadCompletedEventArgs e)
        {

            if (e.Error == null)
            {
                if (e.Result.ToString() == "FileSucces")
                {
                    busyIndicator.BusyContent = "Upload Xml File Completed";
                    busyIndicator.IsBusy = false;
                    MessageBox.Show("Upload Xml File Success !", "Upload Xml To Server Operation", MessageBoxButton.OK);
                }
                else
                {
                    busyIndicator.IsBusy = false;
                    MessageBox.Show("Upload Xml File Error !", "Upload Xml To Server Operation", MessageBoxButton.OK);

                }
            }

        }


        // UPLOAD  XML  FILE  TO FTP  SERVER 
        private void BtnPublishToFtp_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.OpenAsync();
                _client.UploadFileFtpAutomateCompleted -= new EventHandler<UploadFileFtpAutomateCompletedEventArgs>(_client_UploadFileFtpAutomateCompleted);
                _client.UploadFileFtpAutomateCompleted += new EventHandler<UploadFileFtpAutomateCompletedEventArgs>(_client_UploadFileFtpAutomateCompleted);
                busyIndicator.BusyContent = "Upload Xml File To Ftp Server";
                busyIndicator.IsBusy = true;
                _client.UploadFileFtpAutomateAsync("test");

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }


        private void _client_UploadFileFtpAutomateCompleted(object sender, UploadFileFtpAutomateCompletedEventArgs e)
        {

            if (e.Error != null)
            {
                MessageBox.Show(e.Error.InnerException.ToString());
                busyIndicator.BusyContent = "Upload Xml File Completed";
                busyIndicator.IsBusy = false;
            }
            else
            {
                if (e.Result.ToString() == "226 Transfer OK")
                {
                    busyIndicator.BusyContent = "Upload Xml File Completed";
                    busyIndicator.IsBusy = false;
                    MessageBox.Show("Upload Xml File To Ftp Completed !", "Ftp Upload File Operation ", MessageBoxButton.OK);

                }
            }
        }

        private void navPages_PageIndexChanged(object sender, EventArgs e)
        {
        }

       
        public class ListaOrase
        {
            public string CityName { get; set; }
            public string CityId { get; set; }
            public string CountryCode { get; set; }

            public string CountryCodeTeamco { get; set; }
            public string CityAlternative { get; set; }
        }


    }
}