using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using SLaB.Navigation.ContentLoaders.Error;

namespace MoodWeather.Views.Error
{
    public partial class ErrorPage : Page
    {
        public ErrorPage()
        {
            InitializeComponent();
        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            errorInformation.Content = ErrorPageLoader.GetError(this);
            uriLink.NavigateUri = e.Uri;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Clipboard.SetText(ErrorPageLoader.GetError(this).ToString());
        }
    }
}