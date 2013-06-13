using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;

namespace MoodWeather.Views.Admin
{
    public partial class IndexAdmin : Page
    {
        public IndexAdmin()
        {
            InitializeComponent();

            WebContext.Current.Authentication.LoggedOut +=
                (se, ev) => NavigationService.Navigate(new Uri("/Index", UriKind.Relative));
        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }

        private void weatherSetupDomainDataSource_LoadedData(object sender, LoadedDataEventArgs e)
        {
            if (e.HasError)
            {
                MessageBox.Show(e.Error.ToString(), "Load Error", MessageBoxButton.OK);
                e.MarkErrorAsHandled();
            }
        }
    }
}