using System;
using System.Windows.Controls;
using System.Windows.Navigation;
using Telerik.Windows.Controls.GridView;

namespace MoodWeather
{
    /// <summary>
    ///     <see cref="Page" /> class to present information about the current application.
    /// </summary>
    public partial class About : Page
    {
        /// <summary>
        ///     Creates a new instance of the <see cref="About" /> class.
        /// </summary>
        public About()
        {
            InitializeComponent();

            Title ="Weather Raport Status";
        }

        /// <summary>
        ///     Executes when the user navigates to this page.
        /// </summary>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }

        private void viewCityStatuDomainDataSource_LoadedData(object sender, LoadedDataEventArgs e)
        {
            cityTotal.Content = viewCityStatuRadGridView.Items.TotalItemCount;
            busyIndicator.IsBusy = false;
            busyIndicator.BusyContent = "Load Data Completed";
            if (e.HasError)
            {
                busyIndicator.IsBusy = false;
                busyIndicator.BusyContent = "Load Data Completed";
                System.Windows.MessageBox.Show(e.Error.ToString(), "Load Error", System.Windows.MessageBoxButton.OK);
                e.MarkErrorAsHandled();
            }
        }

      

        private void BtnLoadCity_Click_1(object sender, System.Windows.RoutedEventArgs e)
        {
            try
            {
                viewCityStatuDomainDataSource.Load();
                busyIndicator.IsBusy = true;
                busyIndicator.BusyContent = "Load Data";

            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }


      

        private void ViewCityStatuRadGridView_OnFiltering(object sender, GridViewFilteringEventArgs e)
        {
            cityTotal.Content = viewCityStatuRadGridView.Items.TotalItemCount;
            
           
        }
    }
}