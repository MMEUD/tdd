using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;



namespace MoodCamera
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        int timeLeft = 5;
        DispatcherTimer timer = new DispatcherTimer();
        public MainPage()
        {
            this.InitializeComponent();
            NavigationCacheMode =Windows.UI.Xaml.Navigation.NavigationCacheMode.Disabled;
            timer.Interval = new TimeSpan(0, 0, 1);
            timer.Tick += timer_Tick;
            timer.Start();
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }


        void timer_Tick(object sender, object e)
        {
            if (timeLeft > 0)
            {
                timeLeft -= 1;
            }
            else
            {
                timer.Stop();
                this.Frame.Navigate(typeof(CameraPage));
            }
        }


    }
}
