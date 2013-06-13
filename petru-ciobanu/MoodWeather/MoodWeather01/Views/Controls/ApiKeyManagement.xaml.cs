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

namespace MoodWeather.Views.Controls
{
    public partial class ApiKeyManagement : UserControl
    {
        public ApiKeyManagement()
        {
            InitializeComponent();
        }

        private void weatherSetupDomainDataSource_LoadedData(object sender, LoadedDataEventArgs e)
        {

            if (e.HasError)
            {
                System.Windows.MessageBox.Show(e.Error.ToString(), "Load Error", System.Windows.MessageBoxButton.OK);
                e.MarkErrorAsHandled();
            }
        }

        private void BtnSave_Click(object sender, RoutedEventArgs e)
        {
            try
            {
               
                weatherSetupDomainDataSource.SubmitChanges();
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.ToString());
            }
        }

        private void BtnInsert_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                weatherSetupDomainDataSource.QueryName = "InsertWeatherSetup";
                weatherSetupDomainDataSource.SubmitChanges();
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.ToString());
            }


        }
    }
}
